package com.example.mywords.ui.word

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mywords.databinding.ActivityWordBinding
import com.example.mywords.databinding.WordDetailRvItemBinding
import com.example.mywords.sql.AppDatabase
import com.example.mywords.sql.entity.WordTableEntity
import com.example.mywords.ui.WordViewHolder
import kotlinx.coroutines.*

class WordActivity  : AppCompatActivity(){
    private lateinit var binding: ActivityWordBinding
    private lateinit var mWordAdapter: WordAdapter
    val mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        _initView()
        initData()
    }

    private fun _initView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = layoutManager
        binding.rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        mWordAdapter = WordAdapter(0)
        binding.rv.adapter = mWordAdapter
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initData() {
        GlobalScope.launch {
            intent?.extras?.getString("word")?.let {
                val database = AppDatabase.getInstance(this@WordActivity)
                val loadAllWords = database.wordTableDao().loadWordByType3(it)
                loadAllWords.sortBy {
                    it.word
                }
                mWordAdapter.addData(loadAllWords)
                mainScope.launch {
                    mWordAdapter.notifyDataSetChanged()
                    binding.tvTop.text = "${loadAllWords.size}"
                }
            }
        }

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


    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}