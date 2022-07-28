package com.hometask.main.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hometask.app.R
import com.hometask.app.databinding.FragmentHomeBinding
import com.hometask.util.NetworkUtils
import java.net.URLEncoder

/**
 * @description 主页
 * 
 * @date 2022/7/27 5:26 下午
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        private const val HOME_URL = "https://news.163.com"
        private const val SEARCH_URL = "https://www.baidu.com/s?wd="
        private const val LOADING_PROGRESS_MAX = 100
    }
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        initView()
        initObserver()
    }

    private fun initView() {
        loadUrl(HOME_URL)
        initWebView()
        initEditText()
    }

    private fun initWebView() {
        binding.errorTry.setOnClickListener {
            showLoading()
            refreshPage()
        }
        showLoading()

        if (NetworkUtils.isNetworkConnected(binding.root.context).not()) {
            binding.noNetLayout.visibility = View.VISIBLE
            binding.webview.visibility = View.GONE
            hideLoading()
        }

        binding.webview.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true // 这个要加上
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: android.webkit.WebView?, url: String?): Boolean {
                    return false
                }

                override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    hideLoading()
                }

                override fun onReceivedError(
                    view: android.webkit.WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    hideLoading()
                    binding.noNetLayout.visibility = View.VISIBLE
                    binding.webview.visibility = View.GONE
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: android.webkit.WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == LOADING_PROGRESS_MAX) {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun initEditText() {
        binding.search.run {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    binding.ivClearText.visibility =
                        if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                }
            })
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchWord = v.text?.toString()
                    if (searchWord.isNullOrBlank()) { // 搜索词为空的情况不进行搜索
                        true
                    } else {
                        doSearchAction(searchWord)
                        false
                    }
                } else {
                    false
                }
            }
            binding.ivClearText.setOnClickListener {
                (this@run as? TextView)?.text = ""
            }
        }
    }

    private fun doSearchAction(s: String) {
        loadUrl(SEARCH_URL + URLEncoder.encode(s))
    }

    private fun showLoading() {
        binding.noNetLayout.visibility = View.GONE
        binding.topProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        if (!NetworkUtils.isNetworkConnected(binding.root.context)) {
            showError()
        } else {
            binding.webview.visibility = View.VISIBLE
            binding.noNetLayout.visibility = View.GONE
            binding.topProgressBar.visibility = View.GONE
        }
    }

    private fun showError() {
        binding.noNetLayout.visibility = View.VISIBLE
        binding.webview.visibility = View.GONE
    }

    private fun refreshPage() {
        binding.webview.reload()
    }

    private fun initObserver() {
        viewModel.homePageRefreshLiveData.observe(viewLifecycleOwner) {
            loadUrl(HOME_URL)
        }
    }

    private fun loadUrl(url: String) {
        binding.webview.apply {
            hideLoading()
            visibility = View.VISIBLE
            loadUrl(url)
        }
    }
}