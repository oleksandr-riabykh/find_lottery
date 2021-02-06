package com.limestudio.findlottery.presentation.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.svprogresshud.SVProgressHUD
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.extensions.toDateFormat
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import com.limestudio.findlottery.presentation.ui.fullscreen_photo.FullscreenPhotoActivity
import com.limestudio.findlottery.presentation.ui.tickets.draws.DrawsAdapter
import com.limestudio.findlottery.presentation.ui.tickets.draws.SELECTED_DRAW
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*


const val SELECTED_USER = "selected_user"

const val SELECTED_PROFILE = "selected_profile"
class ProfileFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    private val profileViewModel: ProfileViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: DrawsAdapter
    private var mUser: User = User()

    private lateinit var hudSync: SVProgressHUD

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hudSync = SVProgressHUD(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViews()
        initStateListener()
        profileViewModel.loadData(arguments?.get(SELECTED_USER)?.toString())
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getUser(
            arguments?.get(SELECTED_USER)?.toString() ?: Firebase.auth.currentUser?.uid ?: ""
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.edit_profile -> {
                navigateTo(
                    R.id.navigation_edit_profile,
                    R.id.navigation_profile,
                    false,
                    Bundle().apply {
                        putParcelable(SELECTED_PROFILE, mUser)
                    }
                )
                true
            }
            else -> false
        }
    }

    private fun initAdapter() {
        viewAdapter = DrawsAdapter({
            navigateTo(
                R.id.navigation_ticketsFragment,
                R.id.navigation_profile,
                false,
                Bundle().apply {
                    putParcelable(SELECTED_DRAW, it)
                    putString(
                        "title",
                        Date(it.timestamp).toDateFormat(resources.configuration.locales[0])
                    )
                }
            )
        }, {
            profileViewModel.deleteDraw(it)
        })
    }

    private fun initViews() {
        draws_list.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        logout_button.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initStateListener() {
        profileViewModel.user.observe(viewLifecycleOwner, { user ->
            mUser = user
            if (hudSync.isShowing) hudSync.dismiss()
            username_text_view?.text = "${mUser.name} ${mUser.lastName}"
            if (user.city?.isNotEmpty() == true)
                city_text_view?.text =
                    mUser.city.substring(0, 1).toUpperCase(Locale.ROOT) + mUser.city.substring(1)
                        .toLowerCase(Locale.ROOT)
            if (user.nationalId?.isNotEmpty() == true)
                nationalid_text_view?.text =
                    getString(R.string.national_id_s1, mUser.nationalId.toUpperCase(Locale.ROOT))
        }
        )
        profileViewModel.avatarUrl.observe(viewLifecycleOwner, { item ->
            mUser.avatar = item
            if (hudSync.isShowing) hudSync.dismiss()
            if (item.isNotEmpty()) Glide.with(requireActivity()).load(item).into(circleImage)
        }
        )
        profileViewModel.idCardUrl.observe(viewLifecycleOwner, { item ->
            if (hudSync.isShowing) hudSync.dismiss()
            mUser.photoId = item
            id_card_image.setOnClickListener {
                if (item.isNotEmpty()) {
                    val photoIntent = Intent(requireContext(), FullscreenPhotoActivity::class.java)
                    photoIntent.putExtra(FullscreenPhotoActivity.ARG_PHOTO, item)
                    startActivity(photoIntent)
                }
            }
        }
        )
        profileViewModel.draws.observe(viewLifecycleOwner, { items ->
            if (hudSync.isShowing) hudSync.dismiss()
            if (arguments?.get(SELECTED_USER)?.toString()?.isNotEmpty() == true)
                viewAdapter.setViewMode()
            viewAdapter.setData(items)
        }
        )
        profileViewModel.error.observe(viewLifecycleOwner, { item ->
            if (hudSync.isShowing) hudSync.dismiss()
            showWarning(item.messageId)
        }
        )
    }
}
