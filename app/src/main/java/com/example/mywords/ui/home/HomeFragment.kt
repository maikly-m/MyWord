package com.example.mywords.ui.home

import android.content.Intent
import android.graphics.Color.red
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mywords.MainActivity
import com.example.mywords.R
import com.example.mywords.SpUtil
import com.example.mywords.databinding.FragmentHomeBinding
import com.example.mywords.databinding.RootVariantRvItemBinding
import com.example.mywords.sql.AppDatabase
import com.example.mywords.sql.entity.WordTableEntity
import com.example.mywords.ui.WordViewHolder
import com.example.mywords.ui.word.WordActivity
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rootVariantAdapter: RootVariantAdapter
    private lateinit var activity: MainActivity
    private lateinit var type3s: MutableList<WordTableEntity>
    private var position: String? = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = layoutManager
        binding.rv.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        rootVariantAdapter = RootVariantAdapter(this, 0)
        binding.rv.adapter = rootVariantAdapter
        
        binding.tvTop.setOnClickListener {
            if (this::type3s.isInitialized){
                var pos = 0
                type3s.forEachIndexed { index, wordTableEntity ->
                    if (TextUtils.equals(wordTableEntity.type3, position)){
                        pos = index
                        return@forEachIndexed
                    }
                }
                binding.rv.scrollToPosition(pos)
                binding.tvTop.text = "$pos/${type3s.size}"
            }

        }

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    (recyclerView.layoutManager as LinearLayoutManager).let {
                        binding.tvTop.text = "${it.findFirstVisibleItemPosition()}/${type3s.size}"
                    }
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initData() {
        activity = requireActivity() as MainActivity
        //init
        GlobalScope.launch {
            val database = AppDatabase.getInstance(requireContext())
            val loadWordTableSyncByWord = database.wordTableDao().loadWordById(1)
            if (loadWordTableSyncByWord==null){
                delay(2000)
            }
            Log.e(TAG, "initData: start")
            val loadAllWords = database.wordTableDao().loadAllWords()
            Log.e(TAG, "initData: 1")
            loadAllWords.sortBy {
                it.type3
            }
            Log.e(TAG, "initData: 2, size=${loadAllWords.size}")
            type3s = mutableListOf<WordTableEntity>()
            var lastWordTableEntity:WordTableEntity? = null
            loadAllWords.forEach {
                if (lastWordTableEntity?.type3 != it.type3){
                    lastWordTableEntity = it
                    type3s.add(it)
                }
            }
            Log.e(TAG, "initData: 3, size=${type3s.size}")
            rootVariantAdapter.addData(type3s)
            position = SpUtil.getString(requireContext(), "position")
            var pos = 0
            type3s.forEachIndexed { index, wordTableEntity -> 
                if (TextUtils.equals(wordTableEntity.type3, position)){
                    pos = index
                    return@forEachIndexed
                }    
            }
            
            activity.mainScope.launch {
                rootVariantAdapter.notifyDataSetChanged()
                binding.rv.scrollToPosition(pos)
                binding.tvTop.text = "$pos/${type3s.size}"
            }

        }

    }

    internal class RootVariantAdapter(val fragment: HomeFragment, private var size: Int) : RecyclerView.Adapter<WordViewHolder>() {

        lateinit var list: MutableList<WordTableEntity>

        fun addData(list: MutableList<WordTableEntity>){
            size = list.size
            this.list = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
            val binding: RootVariantRvItemBinding = RootVariantRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return WordViewHolder(binding)
        }

        override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
            val b = holder.binding as RootVariantRvItemBinding
            b.tvName.text = list[position].type3
            b.tvMeaning.text = list[position].etymology
            b.root.setOnClickListener {
                fragment.startActivity(Intent(fragment.requireContext(), WordActivity::class.java).apply {
                    putExtra("word", list[position].type3)
                })
                SpUtil.putString(fragment.requireContext(), "position", list[position].type3)
                list.forEachIndexed { index, value ->
                    if (TextUtils.equals(value.type3, fragment.position)){
                        notifyItemChanged(index)
                    }
                }
                fragment.position = list[position].type3
                b.cl.setBackgroundColor(fragment.resources.getColor(R.color.purple_200))
            }
            fragment.position?.let {
                if (TextUtils.equals(it, list[position].type3)){
                    b.cl.setBackgroundColor(fragment.resources.getColor(R.color.purple_200))
                }else{
                    b.cl.setBackgroundColor(fragment.resources.getColor(R.color.white))
                }
            }
        }

        override fun getItemCount(): Int {
            return size
        }
    }

    companion object{
        const val TAG = "HomeFragment"
    }



}