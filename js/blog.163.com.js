function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"gbk");
     var allPattern = java.util.regex.Pattern.compile("(?<=<div class=\"mcnt ztag\">)(.*?)(?=yodaoad)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var temp = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=<span class=\"tcnt\">)(.*?)(?=</span>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE);
		m = titlePattern.matcher(temp);
		if(m.find()){
			var article = new com.hadeslee.webpage.searcher.Article();
			article.setTitle(m.group());
			article.setContent(temp.replaceAll("(?<=<span class=\"tcnt\">)(.*?)(?=</span>)", ""));
			return article;
		}
	 }
	return null;
}

function getSite(){
	return "blog.163.com";
}

function getName(){
	return "ÍøÒ×²©¿Í";
}
