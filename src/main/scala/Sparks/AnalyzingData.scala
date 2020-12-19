package Sparks
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class AnalyzingData {

  def getUsersWhoWrote (context: SparkContext, authors: Array[String]) = {
    val usersWhoWrote = context.parallelize(authors.map(author => (author, 1)))
    val authorsWithCount = usersWhoWrote.groupBy(_._1).mapValues(list => {
      list.map(_._2).sum})
    authorsWithCount.collect()
  }


  def getSumOfWords (context: SparkContext, authorAndWordCounts: Array[(String, Int)]) ={
    val numOfWordsList = context. parallelize(authorAndWordCounts.map(authorAndWordCount => authorAndWordCount))
    val authorWithSumOfWords = numOfWordsList.groupBy(_._1).mapValues(list => {
      list.map(_._2).sum})
    authorWithSumOfWords.collect()
  }

  def getSumOfChars (context: SparkContext, authorsAndCharCounts: Array[(String,Int)]) ={
    val numOfCharsList = context.parallelize(authorsAndCharCounts.map(authorAndCharCount=> (authorAndCharCount)))
    val authorWithSumOfChars = numOfCharsList.groupBy(_._1).mapValues(list => {
      list.map(_._2).sum})
    authorWithSumOfChars.collect()
  }
}
