function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"utf8");
     var allPattern = java.util.regex.Pattern.compile("(?<=<div id=\"articlebody\")(.*?)(?=shareUp)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var temp = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=class=\"titName SG_txta\">)(.*?)(?=</h2>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE);
		m = titlePattern.matcher(temp);
		if(m.find()){
			var article = new com.hadeslee.webpage.searcher.Article();
			article.setTitle(m.group());
			article.setContent(m.replaceAll("<div class=\"articleTag\">"));
			return article;
		}
	 }
	return null;
}

function getSite(){
	return "blog.sina.com.cn";
}

function getName(){
	return "ÐÂÀË²©¿Í";
}

