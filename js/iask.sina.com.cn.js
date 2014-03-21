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
	return "iask.sina.com.cn";
}

function getName(){
	return "爱问知识人3";
}


   function parseTitle(input) {
        var pattern = "(?<=<img src=\"http://i1.sinaimg.cn/pfp/ask/images/zhishi/ssr52a.gif\" alt=\"已解决\" align=\"top\">)(.*?)(?=<span id='zsqprize'>)";
        var temp = com.hadeslee.webpage.util.Util.matcher(input, pattern);
        if (temp != null) {
            return com.hadeslee.webpage.util.Util.matcher(temp, "(?<=<h3>)(.*?)(?=</h3>)");
        }
        return null;
    }

    function parseContent(input) {
        var pattern = "(?<=<div class=\"cl_ans2\">)(.*?)(?=<div class=\"cl_ans\" id=\"iaskrfun\" style=\"display:none;\"></div>)";
        var temp = com.hadeslee.webpage.util.Util.matcher(input, pattern);
        if (temp != null) {
            return com.hadeslee.webpage.util.Util.matcher(temp, "(?<=<div class=\"usr_qus\">)(.*?)(?=<div id=)");
        }
        return null;
    }
