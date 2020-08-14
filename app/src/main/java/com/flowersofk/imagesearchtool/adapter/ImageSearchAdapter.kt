package com.flowersofk.imagesearchtool.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowersofk.imagesearchtool.R
import com.flowersofk.imagesearchtool.data.ImageDoc
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_row_image.view.*
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * RecyclerView Adapter(PagedListAdapter)
 */
class ImageSearchAdapter(private val context: Context) :
    PagedListAdapter<ImageDoc, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageSearchViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as ImageSearchViewHolder).bind(context, repoItem)
        }
    }

    companion object {
        private val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ImageDoc>() {
            override fun areItemsTheSame(oldItem: ImageDoc, newItem: ImageDoc): Boolean =
                // id값이 없으므로 ImageDoc 데이터 중 고유한 값으로 비교
                oldItem.thumbnail_url == newItem.thumbnail_url

            override fun areContentsTheSame(oldItem: ImageDoc, newItem: ImageDoc): Boolean =
                oldItem == newItem
        }
    }

    class ImageSearchViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(context: Context, item: ImageDoc) {

            Glide.with(context).load(item.thumbnail_url).into(containerView.img_thumb)   // 썸네일

            containerView.tv_siteName.setText(item.display_sitename) // 출처
            containerView.tv_collection.setText(item.collection)     // Filter

            // 작성시간
            val zdt = ZonedDateTime.parse(item.datetime, DateTimeFormatter.ISO_DATE_TIME)
            containerView.tv_dateTime.setText(zdt.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))       // 작성시간 ([YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz])

            containerView.setOnClickListener {

                item.doc_url?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    containerView.context.startActivity(intent)
                }

            }

        }

        companion object {
            fun create(parent: ViewGroup): ImageSearchViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_row_image, parent, false)
                return ImageSearchViewHolder(view)
            }
        }

    }

}