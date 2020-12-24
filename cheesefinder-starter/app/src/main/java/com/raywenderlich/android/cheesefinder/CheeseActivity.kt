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

import android.text.Editable
import android.text.TextWatcher
import com.raywenderlich.android.cheesefinder.database.Cheese
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_cheeses.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class CheeseActivity : BaseSearchActivity() {

    private fun createButtonClickObservable(): Observable<String> { //泛型<String>就是变化属性的类型
        return Observable.create { emitter ->   //用emitter为参数，创建一个ObservableOnSubscribe 对象， 用它来定义会变化（被观察）的属性
            searchButton.setOnClickListener {
                emitter.onNext(queryEditText.text.toString())  //通过 emitter.onNext，指明变化属性的值
            }
            emitter.setCancellable {
                searchButton.setOnClickListener(null)
            }
        }
    }

    private fun createTextChangeObservable(): Observable<String> {
        val textChangeObservable = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.toString()?.let {
                        println("asn s=" + it)
                        println("asn onTextChanged, + ${Thread.currentThread().name}")
                        emitter.onNext(it)
                    }
                }

                override fun afterTextChanged(s: Editable?) = Unit
            }
            queryEditText.addTextChangedListener(textWatcher)
            emitter.setCancellable {
                queryEditText.removeTextChangedListener(textWatcher)
            }
        }

        return textChangeObservable.filter {
            it.length >=2
        }.debounce (1000, TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread() )  //debounce默认是在computation schedular
    }

    override fun onStart() {
        super.onStart()

        val buttonClickStream = createButtonClickObservable()
                .toFlowable(BackpressureStrategy.LATEST)
        val textChangeStream = createTextChangeObservable()
                .toFlowable(BackpressureStrategy.BUFFER)
       // val searchTextObservable = Observable.merge<String>(buttonClickStream,textChangeStream)
        val searchTextFlowable = Flowable.merge<String>(buttonClickStream,textChangeStream)

        searchTextFlowable.subscribeOn(AndroidSchedulers.mainThread())    //对应observable变化相关的操作，假如和UI有关，就要在mainthread
                .doOnNext {
                    println("asn in doonnext, + ${Thread.currentThread().name}")
                    showProgress()
                }
                .observeOn(Schedulers.io()) //对于io和网络请求，可以切换到IO thread去操作
                .map {  //在map 里面，通过it 获取到变化的内容. 用于suscribe 前，因为map有可能将obserable 输出的内容类型变化了
                    println("asn in map, + ${Thread.currentThread().name}")
                    cheeseSearchEngine.search(it)
                }
                .observeOn(AndroidSchedulers.mainThread()) //对应observable变化相关的操作，假如和UI有关，就要在mainthread
                .subscribe {    //这里的it是map处理过的结果
                    println("asn in subscribe, + ${Thread.currentThread().name}")
                    hideProgress()
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

//    override fun onStart() {
//        super.onStart()
//
//        val textWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                s?.toString()?.let {
//                    println("asn s=" + it)
//                    println("asn onTextChanged, + ${Thread.currentThread().name}")
//                    showResult(cheeseSearchEngine.search(it))   //对变化的操作也要写在这里，变化和对变化的操作就耦合了，不想rx，将变化的操作交由observer去定义
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) = Unit
//        }
//        queryEditText.addTextChangedListener(textWatcher)
//
//    }
}
