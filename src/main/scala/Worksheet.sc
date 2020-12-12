
import akka.http.scaladsl.model.DateTime
import akka.http.scaladsl.model.DateTime.fromIsoDateTimeString

val message1 = fromIsoDateTimeString("2020-11-29T10:27:06.233Z").get
print(message1)

def date = "[0-9\\-]+(T\\z)".r

"2020-11-02".matches(date.regex)
//def time = "[0-9:]+[0-9.]+(Z\\z)".r
//"12:02:01.025Z".matches(time.regex)

def userName = "%([a-zA-zäÄöÖüÜß0-9]+\\s*)*(%\\z)".r
"%Sebastian Seb123%".matches(userName.regex)

def message = ".*".r
"sdfjkskfsöösk345  1235d4sfsd/\\40()&$§{}".matches(message.regex)

def dateTime = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z".r
"2020-11-29T10:27:06.233Z".matches(dateTime.regex)



