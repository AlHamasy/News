package id.asad.news.data.remote.search

import androidx.paging.PageKeyedDataSource
import id.asad.news.data.remote.network.ApiService
import id.asad.news.data.remote.network.ArticlesItem
import id.asad.news.data.remote.network.ResponseNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsBySearchSource(private val service : ApiService, private val search : String) : PageKeyedDataSource<Int, ArticlesItem>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ArticlesItem>) {
        service.getNewsBySearch(page = 1, search = search).enqueue(object :
            Callback<ResponseNews> {
            override fun onResponse(call: Call<ResponseNews>, response: Response<ResponseNews>) {
                if (response.isSuccessful){
                    val data = response.body()?.articles ?: emptyList()
                    callback.onResult(data, null, 2)
                }
            }
            override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {
        service.getNewsBySearch(page = params.key, search = search).enqueue(object :
            Callback<ResponseNews> {
            override fun onResponse(call: Call<ResponseNews>, response: Response<ResponseNews>) {
                if (response.isSuccessful){
                    val data = response.body()?.articles ?: emptyList()
                    callback.onResult(data, params.key + 1)
                }
            }
            override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {

    }

}