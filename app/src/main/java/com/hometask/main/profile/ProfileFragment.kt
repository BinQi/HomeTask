package com.hometask.main.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.hometask.app.R
import com.hometask.app.databinding.FragmentProfileBinding
import com.hometask.db.entity.AccountEntity
import com.hometask.db.entity.Gender
import com.hometask.ext.toText
import com.hometask.login.LoginActivity
import com.hometask.util.ClickFilter
import com.hometask.util.GlideEngine
import com.hometask.widget.dialog.DialogFactory
import com.hometask.widget.dialog.DialogType
import com.hometask.widget.dialog.PositiveStyle
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener


/**
 * 
 *
 * @description 用户个人信息页
 *
 * @date 2022/7/27 5:26 下午
 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        initView()
        initObserver()
    }

    private fun initView() {
        binding.apply {
            ivUserAvatar.setOnClickListener(ClickFilter {
                handleEditProfile(ProfileType.AVATAR)
            })
            clNicknameContainer.setOnClickListener(ClickFilter {
                handleEditProfile(ProfileType.NICKNAME)
            })
            clGender.setOnClickListener(ClickFilter {
                handleEditProfile(ProfileType.GENDER)
            })
            btAction.setOnClickListener(ClickFilter {
                handleActionBtClick()
            })
        }
    }

    private fun handleActionBtClick() {
        if (viewModel.isLoggedIn()) {
            viewModel.logout()
        } else {
            jumpToLogin()
        }
    }

    private fun jumpToLogin() {
        context?.let { ctx ->
            val intent = Intent(ctx, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        }
    }

    private fun initObserver() {
        viewModel.getAccountLiveData().observe(viewLifecycleOwner) { entity ->
            updateUI(entity)
        }
    }

    private fun updateUI(entity: AccountEntity?) {
        val ctx = context ?: return
        binding.apply {
            val imagePath = entity?.avatarPath
            if (imagePath.isNullOrBlank()) {
                ivUserAvatar.setImageResource(R.drawable.icon_personal_center_default_avatar)
            } else {
                Glide.with(ivUserAvatar)
                    .load(imagePath)
                    .placeholder(R.drawable.icon_personal_center_default_avatar)
                    .into(ivUserAvatar)
            }
            uid.text = entity?.uid ?: "123456"
            tvEditPersonalNickname.text = entity?.nickname ?: "XXXX"
            gender.text = (entity?.gender ?: Gender.NOT_SET).toText(ctx)
            btAction.text = if (entity != null) {
                R.string.logout_txt
            } else {
                R.string.login_txt
            }.let {
                ctx.resources.getString(it)
            }
        }
    }

    private fun handleEditProfile(profileType: ProfileType) {
        if (viewModel.isLoggedIn().not()) {
            jumpToLogin()
            return
        }
        when (profileType) {
            ProfileType.AVATAR -> jumpToAlbum()
            ProfileType.NICKNAME -> showEditNicknameDialog()
            ProfileType.GENDER -> showChooseGenderDialog()
        }
    }

    private fun jumpToAlbum() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setMaxSelectNum(1)
            .setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    result?.firstOrNull()?.let {
                        viewModel.updateAvatar(it.realPath)
                    }
                }
                override fun onCancel() {

                }
            })
    }

    private fun showEditNicknameDialog() {
        val ctx = requireContext()
        DialogFactory.createBuilder(ctx, DialogType.TwoHorizontalBtn).also { b ->
            b.getCustomView()?.let {
                val container = it.findViewById<FrameLayout>(R.id.custom_content_container)
                LayoutInflater.from(ctx).inflate(R.layout.dialog_edit_text, container)
            }
        }
            .setTitle(R.string.edit_nickname)
            .setNegativeBtn(R.string.cancel)
            .setPositiveBtn(R.string.confirm)
            .setPositiveStyle(PositiveStyle.Default)
            .setPositiveBtnClickListener { dialog, _ ->
                (dialog as? Dialog)?.findViewById<EditText>(R.id.edit_text)?.let {
                    viewModel.updateNickname(it.text.toString())
                }
                dialog.dismiss()
            }
            .build()
            .show()
    }

    private fun showChooseGenderDialog() {
        val ctx = requireContext()
        DialogFactory.createBuilder(ctx, DialogType.TwoHorizontalBtn).also { b ->
            b.getCustomView()?.let {
                val container = it.findViewById<FrameLayout>(R.id.custom_content_container)
                LayoutInflater.from(ctx).inflate(R.layout.dialog_spin, container)
            }
        }
            .setTitle(R.string.choose_gender)
            .setNegativeBtn(R.string.cancel)
            .setPositiveBtn(R.string.confirm)
            .setPositiveStyle(PositiveStyle.Default)
            .setPositiveBtnClickListener { dialog, _ ->
                (dialog as? Dialog)?.findViewById<Spinner>(R.id.spinner)?.let { spin ->
                    val selectItem = spin.selectedItem
                    (0 until spin.count).firstOrNull {
                        spin.adapter.getItem(it) == selectItem
                    }?.let {
                        viewModel.updateGender(Gender.from(it))
                    }
                }
                dialog.dismiss()
            }
            .build()
            .show()
    }

    private enum class ProfileType {
        AVATAR,
        NICKNAME,
        GENDER,
    }
}