package com.wwn.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wwn.data.Session
import com.wwn.databinding.FragmentProfileBinding
import com.wwn.ui.auth.LoginActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = Session.user
        binding.tvName.text = user?.nickname ?: "Guest"
        binding.tvPhone.text = user?.phone ?: ""
        binding.btnProfile.setOnClickListener { startActivity(Intent(requireContext(), EditProfileActivity::class.java)) }
        binding.btnSettings.setOnClickListener { startActivity(Intent(requireContext(), SettingsActivity::class.java)) }
        binding.btnHelp.setOnClickListener { startActivity(Intent(requireContext(), HelpActivity::class.java)) }
        binding.btnAbout.setOnClickListener { startActivity(Intent(requireContext(), AboutActivity::class.java)) }
        binding.btnLogout.setOnClickListener {
            Session.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
