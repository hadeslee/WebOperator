function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"utf8");
	 var title = getTitle(s);
	 var content = getContent(s);
	 if(com.hadeslee.webpage.util.Util.isEmpty(title) || com.hadeslee.webpage.util.Util.isEmpty(content)){
		return null;
	 }else{
		var article = new com.hadeslee.webpage.searcher.Article();
		article.setTitle(title);
		article.setContent(content);
		return article;
	 }

}

function getSite(){
	return "wenwen.soso.com";
}

function getName(){
	return "À—À—Œ Œ ";
}

function getTitle(s){
	var titlePattern = java.util.regex.Pattern.compile("(?<=<h4 id=\"questionTitle\">)(.*?)(?=</h4>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE);
	var m = titlePattern.matcher(s);
	if (m.find()) {
            var temp = m.group();
            temp = temp.replace("<h3>", "");
            return temp.trim();
        }
        return null;
}

 
function getContent(s){
   var contentPattern_1 = java.util.regex.Pattern.compile("(?<=<div class=\"sloved_answer\">)(.*?)(?=<div class=\"evaluation_wrap\">)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.MULTILINE);
   var contentPattern_2 = java.util.regex.Pattern.compile("(?<=<pre>)(.*?)(?=</pre>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.MULTILINE);

	var  m = contentPattern_1.matcher(s);
        if (m.find()) {
           var temp = m.group();
            m = contentPattern_2.matcher(temp);
            if (m.find()) {
                return m.group();
            }
        }
        return null;
}

