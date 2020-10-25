/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.cheesefinder

import com.raywenderlich.android.cheesefinder.database.Cheese
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_cheeses.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlinx.coroutines.*


class CheeseActivity : BaseSearchActivity() {

    private fun createButtonClickObservable(): Observable<String> {
        return Observable.create { emitter ->
            searchButton.setOnClickListener {
                emitter.onNext(queryEditText.text.toString())
            }
            emitter.setCancellable {
                searchButton.setOnClickListener(null)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val searchTextObservable = createButtonClickObservable()

//        searchTextObservable.subscribe{ query ->
//            showResult(cheeseSearchEngine.search(query))
//
//        }

        searchTextObservable.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map {
                    println("asn in map, + ${Thread.currentThread().name}")
                    cheeseSearchEngine.search(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    println("asn in subscribe, + ${Thread.currentThread().name}")
                    showResult(it)
                }
    }


//    override fun onStart() {
//        super.onStart()
//
//        searchButton.setOnClickListener {
////            showResult(cheeseSearchEngine.search(queryEditText.text.toString()))
//            println("asn b4 aync, + ${Thread.currentThread().name}")
//            val searchResult: Deferred<List<Cheese>> = GlobalScope.async{
//                println("asn in aync, + ${Thread.currentThread().name}")
//                cheeseSearchEngine.search(queryEditText.text.toString())
//            }
//
//            println("asn after aync, + ${Thread.currentThread().name}")
//            runBlocking {
//                println("asn in runblockng, + ${Thread.currentThread().name}")
//                showResult(searchResult.await().toList())
//            }
//            println("asn after runblock, + ${Thread.currentThread().name}")
//        }
//    }
}
