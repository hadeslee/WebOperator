function readArticle(url){
	  var s = com.hadeslee.webpage.util.Util.readUrl(url, "gb2312");
      var title = parseTitle(s);
      if (title != null) {
           var c = parseContent(s);
           if (c != null) {
                var a = new com.hadeslee.webpage.searcher.Article();
                a.setTitle(title);
                a.setContent(c);
                return a;
            }
       }
	return null;
}

function getSite(){
	return "news.qq.com";
}

function getName(){
	return "QQĞÂÎÅ";
}


   function parseTitle(input) {
        var pattern = "(?<=<div id=\"ArticleTit\">)(.*?)(?=</div>)";
        var temp = com.hadeslee.webpage.util.Util.matcher(input, pattern);
        return temp;
    }

    function parseContent(input) {
        var pattern = "(?<=<div id=\"ArticleCnt\">)(.*?)(?=<div id=\"ArtPLink\">)";
        var temp = com.hadeslee.webpage.util.Util.matcher(input, pattern);
        if (temp != null) {
            return temp.replaceAll("(?sm)(?<=<div id=\"Reading\">)(.*?)(?=</table>\\s*</div>)", "");
        }
        return null;
    }
