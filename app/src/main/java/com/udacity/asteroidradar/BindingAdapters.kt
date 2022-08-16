package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidItemsAdapter

 //handling recview adapter
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidItemsAdapter
    adapter.submitList(data)
}


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription=R.string.potentially_hazardous_asteroid_image.toString()
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription=R.string.not_hazardous_asteroid_image.toString()
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription=R.string.potentially_hazardous_asteroid_image.toString()
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription=R.string.not_hazardous_asteroid_image.toString()
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("setNameText")
fun TextView.setNameText(item: Asteroid?) {
    item?.let {
        text = item.codename
    }
}

@BindingAdapter("setDateText")
fun TextView.setDateText(item: Asteroid?) {
    item?.let {
        text = item.closeApproachDate
    }
}

@BindingAdapter("imageUrl")
fun bindPictureOfDay(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
     Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_picture_of_day)
           .error(R.drawable.ic_baseline_broken_image_24)
           .placeholder(R.drawable.loading_animation)
     .into(imageView)

    }
}


