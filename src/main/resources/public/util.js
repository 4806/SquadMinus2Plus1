/**
 * Created by david on 02/03/17.
 */
function pageUtil(){}

pageUtil.validateEmail = function(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
};

pageUtil.validateAlphaNum = function validateCode(code){
    return code.match(/^[0-9a-z]+$/);
};