package Sparks
import org.apache.spark.SparkContext

class AnalyzingData {

  def getUsersWhoWrote (context: SparkContext, authors: Array[String]) = {
    val usersWhoWrote = context.parallelize(authors.map(author => (author, 1)))
    val authorsWithCount = usersWhoWrote.groupBy(_._1).mapValues(list => {
      list.map(_._2).sum})
    authorsWithCount.collect()
  }
}
