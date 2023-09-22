package it.fox.geofencepoc

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import it.fox.geofencepoc.R

class HomeFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ids=view.findViewById<View>(R.id.ids_generation)
        val geofences=view.findViewById<View>(R.id.geofences)
        ids.setOnClickListener {v->navigateTo(KeyFragment())}
        geofences.setOnClickListener { v -> navigateTo(MapFragment())}
    }

    fun navigateTo(fragment: Fragment){
        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
        }
    }
}