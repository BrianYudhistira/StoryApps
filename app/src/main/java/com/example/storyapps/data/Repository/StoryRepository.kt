import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapps.api.ListStoryItem
import com.example.storyapps.api.Story
import com.example.storyapps.api.config.ApiConfig
import kotlinx.coroutines.flow.Flow

class StoryRepository() {

    fun getStories(token: String): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { StoryPagingSource(token) }
        ).flow
    }

    suspend fun getDetail(token: String, id: String): Story {
        val response = ApiConfig.getApiService(token).getDetailStory(id)
        if (response.error) {
            throw Exception(response.message)
        }
        return response.story
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
