package it.fox.geofencepoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import it.fox.geofencepoc.domain.UserData
import it.fox.geofencepoc.viewmodel.GeofenceViewModel
import it.fox.geofencepoc.viewmodel.UserDataViewModel
import it.fox.geofencepoc.R

class KeyFragment : Fragment() {

    private lateinit var viewModel: UserDataViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = UserDataViewModel()
        val view = inflater.inflate(R.layout.keys_layout, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn = view.findViewById<View>(R.id.gen_btn)
        btn.setOnClickListener { v ->
            run {
                val userdata = UserData()
                userdata.deviceId = UniqueDeviceId.getUniqueId()
                userdata.iv = userdata.generateIv()
                userdata.privateKey = userdata.generateKey()
                viewModel.insert(userdata)
            }
        }
        val keysContainer = view.findViewById<View>(R.id.keys_container)
        val key=view.findViewById<TextView>(R.id.pv_key_value)
        val iv=view.findViewById<TextView>(R.id.pv_iv_value)
        viewModel.allUserData.observe(viewLifecycleOwner){
            datalist->
            run {
                if (datalist != null && datalist.isNotEmpty()) {
                    val userData = datalist.get(datalist.size - 1)
                    key.text=userData.privateKey
                    iv.text=userData.iv
                    keysContainer.visibility=View.VISIBLE
                } else {
                    keysContainer.visibility = View.GONE
                }
            }
        }
    }
}