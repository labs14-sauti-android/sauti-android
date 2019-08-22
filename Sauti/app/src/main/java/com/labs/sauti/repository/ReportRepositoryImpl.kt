package com.labs.sauti.repository

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.labs.sauti.api.SautiApiService
import com.labs.sauti.model.report.ReportForm
import com.labs.sauti.model.report.Border
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class ReportRepositoryImpl(private val sautiApiService: SautiApiService) : ReportRepository {
    override fun getBorderNames(): Single<MutableList<String>> {
        // TODO test only
        return Single.fromCallable {
            val request = Request.Builder()
                .url("http://sautiafrica.org/endpoints/api.php?url=v1/borderCrossings/&type=json")
                .build()
            val responseBody = OkHttpClient.Builder().build()
                .newCall(request)
                .execute()
                .body()

            responseBody ?: throw Exception("No response")

            val responseStr = responseBody.string()

            val gson = GsonBuilder().create()
            val typeToken = object: TypeToken<MutableList<Border>>() {}.type
            val borders = gson.fromJson<MutableList<Border>>(responseStr, typeToken)

            borders.mapNotNull {
                val borderName = if (it.borderCountries.size > 1) { // can have three countries
                    buildString {
                        append(it.borderName)
                        append(" ")
                        append("(")
                        it.borderCountries.forEach {borderCountry ->
                            append(borderCountry)
                            append("/")
                        }
                        setLength(length - 1)
                        append(")")
                    }
                } else {
                    it.borderName
                }
                borderName
            }.toMutableList()
        }
            .subscribeOn(Schedulers.io())
    }

    override fun submitReportForm(reportForm: ReportForm): Completable {
        // TODO test only
        return Completable.complete()
    }
}
