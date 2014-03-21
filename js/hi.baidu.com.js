function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"gb2312");
     var allPattern = java.util.regex.Pattern.compile("(?<=<div id=\"m_blog\" class=\"modbox\" style=\"overflow-x:hidden;\">)(.*?)(?=<div class=\"opt\">)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var temp = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=<div class=\"tit\">)(.*?)(?=</div>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE| java.util.regex.Pattern.MULTILINE);
		m = titlePattern.matcher(temp);
		if(m.find()){
			var article = new com.hadeslee.webpage.searcher.Article();
			article.setTitle(m.group());
			article.setContent(temp.replaceAll("<div class=\"tit\">(.*?)</div>", ""));
			return article;
		}
	 }
	return null;
}

function getSite(){
	return "hi.baidu.com";
}

function getName(){
	return "°Ù¶ÈHI";
}
