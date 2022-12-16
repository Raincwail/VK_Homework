package com.example.vk_homework

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.navigation.fragment.findNavController
import com.example.vk_homework.databinding.FragmentFirstBinding
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        parentFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            val result = bundle.getString("data")
            // Do something with the result..
        }

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN = (activity as MainActivity).TOKEN

        binding.buttonFirst.setOnClickListener {

            var vkId = binding.editTextVKID.text.toString()

            if (vkId.isEmpty()) {
                binding.editTextVKID.error = "ID required"
                binding.editTextVKID.requestFocus()
                return@setOnClickListener
            }

            var k : String

            if (vkId.isDigitsOnly()){
                k = (activity as MainActivity).asyncRun("https://api.vk.com/method/photos.getAlbums?v=5.131&access_token=$TOKEN&owner_id=$vkId").get()

                if (k.substring(2, 7) == "error") {
                    binding.editTextVKID.error = "Probably Wrong User Id"
                    binding.editTextVKID.requestFocus()
                    return@setOnClickListener
                }

            } else {
                k = (activity as MainActivity).asyncRun("https://api.vk.com/method/users.get?v=5.131&access_token=$TOKEN&user_ids=$vkId").get()

                if (k.substring(2, 7) == "error") {
                    binding.editTextVKID.error = "Probably Wrong Username Case"
                    binding.editTextVKID.requestFocus()
                    return@setOnClickListener
                }

                val temp = JSONObject(k).getJSONArray("response")

                if (temp.isNull(0)){
                    binding.editTextVKID.error = "VK API returned an empty response"
                    binding.editTextVKID.requestFocus()
                    return@setOnClickListener
                }

                vkId = (temp[0] as JSONObject).getInt("id").toString()

                k = (activity as MainActivity).asyncRun("https://api.vk.com/method/photos.getAlbums?v=5.131&need_system=1&access_token=$TOKEN&owner_id=$vkId").get()
            }

            if (k.substring(2, 7) == "error") {
                binding.editTextVKID.error = "Probably access restricted by the user"
                binding.editTextVKID.requestFocus()
                return@setOnClickListener
            }

            Log.println(Log.VERBOSE, "", k)

            val jsonObj = JSONObject(k)
            val albumList = (jsonObj.toMap()["response"] as LinkedHashMap<String, *>)["items"] as ArrayList<Map<String, *>>

            var albumTitles = arrayListOf<String>()
            var albumDescs = arrayListOf<String>()
            var photoURLs = arrayListOf<ArrayList<String>>()

            if (albumList.isEmpty()){
                binding.editTextVKID.error = "Sadly, this user does not have any albums"
                binding.editTextVKID.requestFocus()
                return@setOnClickListener
            }

            for (curAlbum in albumList){
                val albumId = curAlbum["id"]
                val albumPhotos = (activity as MainActivity).asyncRun("https://api.vk.com/method/photos.get?v=5.131&access_token=$TOKEN&owner_id=$vkId&album_id=$albumId").get()

                if (albumPhotos.substring(2, 7) == "error"){
                    continue
                }

                val jsonPhotos = JSONObject(albumPhotos)
                var photoList = (jsonPhotos.toMap()["response"] as Map<String, *>)["items"] as ArrayList<Map<String, *>>
                val albumPhotoURLs = arrayListOf<String>()

                for (curPhoto in photoList){
                    albumPhotoURLs.add(
                        (
                            (
                                curPhoto["sizes"] as ArrayList<Map<*, *>>
                            )
                            [2] as Map<String, *>)
                        ["url"] as String)
                }

                photoURLs.add(albumPhotoURLs)
                albumTitles.add(curAlbum["title"] as String)

                if (curAlbum["description"] != null) {
                    albumDescs.add(curAlbum["description"] as String)
                } else {
                    albumDescs.add("No description")
                }

            }

            (activity as MainActivity).myBundle.putStringArrayList("album_titles", albumTitles)
            (activity as MainActivity).myBundle.putStringArrayList("album_descs", albumDescs)
            (activity as MainActivity).myBundle.putSerializable("photo_urls", photoURLs)

            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toMutableList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}