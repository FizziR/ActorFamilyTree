package Sparks

import org.apache.spark.SparkContext

class AnalyzingData {

  def getUsersWhoWrote (context: SparkContext, dataList: List[(String, Int, Int)]): Array[(String, Int)] = {
    val usersWhoWrote = context.parallelize(dataList.map(data => (data._1, 1)))
    val authorsWithCount = usersWhoWrote.groupBy(_._1).mapValues(list => {
      list.map(_._2).sum})
    authorsWithCount.collect()
  }


  def getSumOfWords (context: SparkContext, dataList: List[(String, Int, Int)]): Array[(String, Int)] ={
    val numOfWordsList = context. parallelize(dataList.map(data => (data._1, data._2)))
    val authorWithSumOfWords = numOfWordsList.groupBy(_._1).mapValues(list => {
      list.map(_._2).sum})
    authorWithSumOfWords.collect()
  }

  def getSumOfChars (context: SparkContext, dataList: List[(String,Int, Int)]): Array[(String, Int)] ={
    val numOfCharsList = context.parallelize(dataList.map(data=> (data._1, data._3)))
    val authorWithSumOfChars = numOfCharsList.groupBy(_._1).mapValues(list => {
      list.map(_._2).sum})
    authorWithSumOfChars.collect()
  }
}
