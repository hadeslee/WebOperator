function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"utf8");
     var allPattern = java.util.regex.Pattern.compile("(?<=<section id=\"exp-detail\" class=\"border-padding\">)(.*?)(?=<h2>参考资料</h2>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var temp = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=<span class=\"exp-title\">)(.*?)(?=</span>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE| java.util.regex.Pattern.MULTILINE);
		m = titlePattern.matcher(temp);
		if(m.find()){
			var article = new com.hadeslee.webpage.searcher.Article();
			article.setTitle(m.group());
			article.setContent(temp);
			return article;
		}
	 }
	return null;
}

function getSite(){
	return "jingyan.baidu.com";
}

function getName(){
	return "百度经验";
}
