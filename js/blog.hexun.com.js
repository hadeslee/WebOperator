function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"gb2312");
     var allPattern = java.util.regex.Pattern.compile("(?<=<div class=\"Article\" id=\"ArticeTextID\">)(.*?)(?=<div class=\"moreArticles\">)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var temp = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=<span class=\"ArticleTitleText\">)(.*?)(?=</span>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE);
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
	return "blog.hexun.com";
}

function getName(){
	return "��Ѷ����";
}

