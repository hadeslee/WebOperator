function readArticle(url){
	 var s = com.hadeslee.webpage.util.Util.readUrl(url,"gbk");
     var allPattern = java.util.regex.Pattern.compile("(?<=<div class=\"wz_06\">)(.*?)(?=<div align=\"left\" class=\"link_c\">)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.MULTILINE);
	 var m = allPattern.matcher(s);
	 if(m.find()){
		var temp = m.group();
		var titlePattern = java.util.regex.Pattern.compile("(?<=<h1 style=\"display:inline\">)(.*?)(?=</h1>)", java.util.regex.Pattern.DOTALL | java.util.regex.Pattern.CASE_INSENSITIVE);
		m = titlePattern.matcher(temp);
		if(m.find()){
			var article = new com.hadeslee.webpage.searcher.Article();
			article.setTitle(m.group());
			article.setContent(temp.replaceAll("<h1 style=\"display:inline\">(.*?)</h1>", ""));
			return article;
		}
	 }
	return null;
}

function getSite(){
	return "blog.china.alibaba.com";
}

function getName(){
	return "∞¢¿Ô∞Õ∞Õ";
}
