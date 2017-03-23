/**
 * Created by david on 02/03/17.
 */
function pageUtil(){}

pageUtil.validateEmail = function(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
};

pageUtil.validateAlphaNum = function validateCode(code){
    return code.match(/^[0-9A-Za-z]+$/) !== null;
};

//This will extract all of the parameters included in a URL
pageUtil.getUrlContent = function(url) {
    var questionIndex = url.lastIndexOf("?");
    if( questionIndex === -1 ) {
        return {};
    }

    var content = url.substr( questionIndex + 1);
    if( content === "" ) {
        return {};
    }

    var paramsRaw = content.split("&");
    var params = {};
    for( var i = 0; i < paramsRaw.length; i++ ){
        var vals = paramsRaw[i].split("=");
        params[vals[0]] = vals[1];
    }
    return params;
};

pageUtil.getFormattedDate = function(epoch) {
  var date = new Date(epoch);
  var year = date.getFullYear();
  var month = date.getMonth();
  var day = date.getDay();
  var hour = date.getHours();
  var minute = date.getMinutes();
  var timezone = date.toString().match(/\(([A-Za-z\s].*)\)/)[1];

  return day + "/" + month + "/" + year + " at " + hour + ":" + minute + " " + timezone;
};
