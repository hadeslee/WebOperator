function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"gb2312");
     var allPattern = java.util.regex.Pattern.compile("(?<=<span class=\"question-title\">)(.*?)(?=</span>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var sTitle = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=<pre id=\"best-answer-content\" class=\"reply-text mb10\">)(.*?)(?=</pre>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE| java.util.regex.Pattern.MULTILINE);
		m = titlePattern.matcher(s);
		if(m.find()){
			var sContent = m.group();
			var article = new com.hadeslee.webpage.searcher.Article();
			article.setTitle(sTitle);
			article.setContent(sContent);
			return article;
		}
	 }
	return null;
}

function getSite(){
	return "zhidao.baidu.com";
}

function getName(){
	return "百度知道";
}
