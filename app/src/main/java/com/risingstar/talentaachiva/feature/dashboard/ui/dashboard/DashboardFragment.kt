package com.risingstar.talentaachiva.feature.dashboard.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.risingstar.talentaachiva.R
import com.risingstar.talentaachiva.databinding.FragmentDashboardBinding
import com.risingstar.talentaachiva.domain.data.Event
import com.risingstar.talentaachiva.feature.dashboard.DashboardVM
import com.risingstar.talentaachiva.feature.detail.DetailActivity
import com.risingstar.talentaachiva.feature.management.ManagementActivity
import com.risingstar.talentaachiva.feature.organizer.OrganizerActivity
import com.risingstar.talentaachiva.feature.util.EventAdapter
import com.risingstar.talentaachiva.feature.util.ImageAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var rvEvents: RecyclerView
    private lateinit var rvRecommend : RecyclerView
    private lateinit var rvRec : RecyclerView
    private lateinit var rvAdapter: EventAdapter
    private lateinit var viewmodel: DashboardVM

    val images = mutableListOf(
        "https://cdn.discordapp.com/attachments/985110137750585415/985438540206837760/unknown.png",
        "https://cdn.discordapp.com/attachments/985110137750585415/985438607227629568/unknown.png",
        "https://cdn.discordapp.com/attachments/985110137750585415/985438627343519784/unknown.png"
    )

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(requireActivity()).get(DashboardVM::class.java)
        rvEvents = binding.rvEvent
        rvEvents.layoutManager = GridLayoutManager(this.context,1,RecyclerView.HORIZONTAL,false)

        rvRecommend = binding.rvBanner
        rvRecommend.layoutManager = GridLayoutManager(this.context,1,RecyclerView.HORIZONTAL,false)

        rvRec = binding.rvBanner
        rvRec.layoutManager = GridLayoutManager(this.context,1,RecyclerView.HORIZONTAL,true)

        with(binding){
            searchView.setOnClickListener {
                findNavController().navigate(R.id.navigation_search)
            }

            imgOrg.setOnClickListener {
                val intent = Intent(requireActivity(),OrganizerActivity::class.java)
                intent.putExtra(OrganizerActivity.CURRENT_USER_ID,viewmodel.userID)
                startActivity(intent)
            }
        }

        viewmodel.allEvents().observe(viewLifecycleOwner){
            rvAdapter = EventAdapter(it as ArrayList<Event>)
            rvEvents.adapter = rvAdapter
            rvAdapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Event) {
                    showSelected(data)
                }
            })
            rvRec.adapter = rvAdapter

            rvRecommend.adapter = ImageAdapter(images as ArrayList<String>)
        }

        return binding.root
    }

    private fun showSelected(event: Event) {
        if(event.participants?.contains(viewmodel.userID) != true){
            val intent = Intent(this.context,DetailActivity::class.java)
            intent.putExtra(DetailActivity.CURRENT_EVENT,event)
            intent.putExtra(DetailActivity.CURRENT_USER,viewmodel.userID)
            startActivity(intent)
        }else{
            val intent = Intent(this.context,ManagementActivity::class.java)
            intent.putExtra(ManagementActivity.CURRENT_EVENT,event.eventId)
            intent.putExtra(ManagementActivity.CURRENT_USER,viewmodel.userID)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}