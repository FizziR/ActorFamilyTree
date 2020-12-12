


def date = "[0-9\\-]+(T\\z)".r

"2020-11-02".matches(date.regex)
//def time = "[0-9:]+[0-9.]+(Z\\z)".r
//"12:02:01.025Z".matches(time.regex)

def userName = "%([a-zA-zäÄöÖüÜß0-9]+\\s*)*(%\\z)".r
"%Sebastian Seb123%".matches(userName.regex)

def message = ".*".r
"sdfjkskfsöösk345  1235d4sfsd/\\40()&$§".matches(message.regex)

def time = "[0-9\\-]+T[0-9:]+[0-9.]+(Z\\z)".r
"2020-11-29T10:27:06.233Z".matches(time.regex)
