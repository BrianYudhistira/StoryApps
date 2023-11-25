import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapps.api.ListStoryItem
import com.example.storyapps.api.config.ApiConfig

class StoryPagingSource(private val token: String) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            Log.d("StoryPagingSource", "Loading page $page")
            val responseData = ApiConfig.getApiService(token).getAllStory(page, params.loadSize).listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Log.e("StoryPagingSource", "Error loading page", exception)
            return LoadResult.Error(exception)
        }
    }
}
