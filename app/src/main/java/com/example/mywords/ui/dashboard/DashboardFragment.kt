package com.example.mywords.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mywords.MainActivity
import com.example.mywords.databinding.FragmentDashboardBinding
import com.example.mywords.databinding.WordDetailRvItemBinding
import com.example.mywords.sql.AppDatabase
import com.example.mywords.sql.entity.WordTableEntity
import com.example.mywords.ui.WordViewHolder
import kotlinx.coroutines.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var wordAdapter:WordAdapter
    private lateinit var activity: MainActivity

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity = requireActivity() as MainActivity

        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = layoutManager
        binding.rv.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        wordAdapter = WordAdapter(0)
        binding.rv.adapter = wordAdapter

        binding.tvSubmit.setOnClickListener { _ ->
            binding.tvSearch.text.let {
                it.trim().let { c ->
                    if (!TextUtils.isEmpty(c)){
                        //search
                        searchWords(c.toString())
                    }
                }

            }
        }

        binding.tvSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                //search
                s?.let {
                    it.trim().let { c ->
                        if (!TextUtils.isEmpty(c)){
                            //search
                            searchWords(c.toString())
                        }
                    }
                }
            }
        })

        return root
    }
    private var async: Job? = null
    private fun searchWords(s: String) {

        val runnable = Runnable {
            activity.mainScope.launch(Dispatchers.IO) {
                val database = AppDatabase.getInstance(requireContext())
                //匹配前缀
                val loadAllWords = database.wordTableDao().loadWordsByPre(s)
                loadAllWords.sortBy {
                    it.word
                }
                wordAdapter.addData(loadAllWords)
                activity.mainScope.launch {
                    wordAdapter.notifyDataSetChanged()
                    binding.tvTop.text = "${loadAllWords.size}"
                }
            }
        }

        async?.let {
            if (!it.isCompleted){
                Log.e("DashboardFragment", "searchWords: async cancel" )
                it.cancel()
            }
        }
        async = activity.mainScope.async(Dispatchers.IO) {
            runnable.run()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    internal class WordAdapter(private var size:Int) : RecyclerView.Adapter<WordViewHolder>() {

        lateinit var list: MutableList<WordTableEntity>

        fun addData(list: MutableList<WordTableEntity>){
            size = list.size
            this.list = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
            val binding: WordDetailRvItemBinding = WordDetailRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return WordViewHolder(binding)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
            val b = holder.binding as WordDetailRvItemBinding
            b.tvWordName.text = list[position].word
            b.tvSoundMark.text = list[position].soundmark
            b.tvRootVariant.text = "词根:${list[position].rootvariant?.split("。")?.get(0)}"
            b.tvStructure.text = "拆分:"+list[position].structure
            b.tvExplain.text = list[position].explain
            b.tvTranslation.text = "释义:"+list[position].translation
            b.tvMeaning.text = "单词翻译:"+list[position].meaning

            b.root.setOnClickListener {

            }
        }

        override fun getItemCount(): Int {
            return size
        }
    }
}