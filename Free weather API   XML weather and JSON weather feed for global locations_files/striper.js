function striper(parentElementTag,parentElementClass,childElementTag,styleClasses)
{var i=0,currentParent,currentChild;if((document.getElementsByTagName)&&(parentElementTag)&&(childElementTag)&&(styleClasses)){var styles=styleClasses.split(',');var parentItems=document.getElementsByTagName(parentElementTag);while(currentParent=parentItems[i++]){if((parentElementClass==null)||(currentParent.className==parentElementClass)){var j=0,k=0;var childItems=currentParent.getElementsByTagName(childElementTag);while(currentChild=childItems[j++]){k=(j+(styles.length-1))%styles.length;currentChild.className=currentChild.className+" "+styles[k];}}}}}
function forceClick(e,elemId){var elem=document.getElementById(elemId);var evt=(e)?e:window.event;var intKey=(evt.which)?evt.which:evt.keyCode;if(intKey==13){elem.click();return false;}
return true;}
var BrowserDetect={init:function(){this.browser=this.searchString(this.dataBrowser)||"An unknown browser";this.version=this.searchVersion(navigator.userAgent)||this.searchVersion(navigator.appVersion)||"an unknown version";this.OS=this.searchString(this.dataOS)||"an unknown OS";},searchString:function(data){for(var i=0;i<data.length;i++){var dataString=data[i].string;var dataProp=data[i].prop;this.versionSearchString=data[i].versionSearch||data[i].identity;if(dataString){if(dataString.indexOf(data[i].subString)!=-1)
return data[i].identity;}
else if(dataProp)
return data[i].identity;}},searchVersion:function(dataString){var index=dataString.indexOf(this.versionSearchString);if(index==-1)return;return parseFloat(dataString.substring(index+this.versionSearchString.length+1));},dataBrowser:[{string:navigator.userAgent,subString:"OmniWeb",versionSearch:"OmniWeb/",identity:"OmniWeb"},{string:navigator.vendor,subString:"Apple",identity:"Safari"},{prop:window.opera,identity:"Opera"},{string:navigator.vendor,subString:"iCab",identity:"iCab"},{string:navigator.vendor,subString:"KDE",identity:"Konqueror"},{string:navigator.userAgent,subString:"Firefox",identity:"Firefox"},{string:navigator.vendor,subString:"Camino",identity:"Camino"},{string:navigator.userAgent,subString:"Netscape",identity:"Netscape"},{string:navigator.userAgent,subString:"MSIE",identity:"Explorer",versionSearch:"MSIE"},{string:navigator.userAgent,subString:"Gecko",identity:"Mozilla",versionSearch:"rv"},{string:navigator.userAgent,subString:"Mozilla",identity:"Netscape",versionSearch:"Mozilla"}],dataOS:[{string:navigator.platform,subString:"Win",identity:"Windows"},{string:navigator.platform,subString:"Mac",identity:"Mac"},{string:navigator.platform,subString:"Linux",identity:"Linux"}]};BrowserDetect.init();function setSearchOverlay()
{var _width=750;var _height=screen.availHeight;if(BrowserDetect.browser=='Explorer'&&BrowserDetect.version<=6)
{}}
function Cover(bottom,top,ignoreSize){var location=Sys.UI.DomElement.getLocation(bottom);top.style.position='absolute';top.style.top='-110px';top.style.left='10px';if(!ignoreSize){top.style.height=bottom.offsetHeight+'px';top.style.width=bottom.offsetWidth+'px';}}
function Resize(bottom){bottom.style.width=screen.availWidth-130;bottom.style.height=screen.availHeight-200;}
function setAjaxLoaderOff(){var updateProgressDiv=$get('updateProgressDivForecastData');if(updateProgressDiv!=null)
updateProgressDiv.style.display='none';}
function onUpdatingUpdatePanel(updateProgressDiv,gridView){updateProgressDiv.style.display='';var gridViewBounds=Sys.UI.DomElement.getBounds(gridView);var updateProgressDivBounds=Sys.UI.DomElement.getBounds(updateProgressDiv);var x=gridViewBounds.x+Math.round(gridViewBounds.width/2)-Math.round(updateProgressDivBounds.width/2);var y=gridViewBounds.y+Math.round(gridViewBounds.height/10);Sys.UI.DomElement.setLocation(updateProgressDiv,x,y);}
function onUpdatedUpdatePanel(updateProgressDiv){if(updateProgressDiv!=null)
updateProgressDiv.style.display='none';}
function setWeatherActivityID(id){var v=document.getElementById('weatheractivity1_activity_id');v.value=id;}
function showModalPopupOnPageLoad()
{var modalPopupBehavior=$find('programmaticModalGaleWarningPopupBehavior');if(modalPopupBehavior!=null)
modalPopupBehavior.show();}
function showLoaderOnMapShape(){var sh=null;if($get('ajax_loader_vmap'))
{sh=$get('ajax_loader_vmap');sh.style.display='';}
else
alert($get('ajax_loader_vmap'));}
function createCookie(name,value,days){if(days){var date=new Date();date.setTime(date.getTime()+(days*24*60*60*1000));var expires="; expires="+date.toGMTString();}
else var expires="";document.cookie=name+"="+value+expires+"; path=/";}
function eraseCookie(name){createCookie(name,"",-1);}