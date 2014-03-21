function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"utf-8");
     var allPattern = java.util.regex.Pattern.compile("(?<=<div class=\"lemmaTitleH1\")(.*?)(?=<div id=\"open-tag\")", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var temp = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=>)(.*?)(?=</span)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE);
		m = titlePattern.matcher(temp);
		if(m.find()){
			var article = new com.hadeslee.webpage.searcher.Article();
			article.setTitle(m.group());
			article.setContent(temp.replaceAll("<h1 class=\"title\">(.*?)</h1>", ""));
			return article;
		}
	 }
	return null;
}

function getSite(){
	return "baike.baidu.com";
}

function getName(){
	return "百度百科";
}

