package net.tctelco.vahalaru.pollsbybropro.Repository

import android.arch.lifecycle.LiveData
import com.bumptech.glide.load.engine.Resource
import net.tctelco.vahalaru.pollsbybropro.Model.Questions

/**
 * Created by jerem on 2/5/2018.
 */
interface Repository{
    fun loadQuestions(): LiveData<Resource<List<Questions>>>
}