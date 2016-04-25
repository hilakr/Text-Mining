library(RCurl)
library(RJSONIO)
library(plyr)
library(bitops)
library(jsonlite)
require(data.table)
library(rworldmap)

#get the data
jsonData = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=295548&APPID=0790aa655754a712e0fb78e2fa2277c7")
names(jsonData)

#get current weather of my city bat yam
wind = jsonData$wind$speed
cloudiness = jsonData$clouds$all
humidity = jsonData$main$humidity
temp = jsonData$main$temp - 273.15
temp_max = jsonData$main$temp_max - 273.15
temp_min = jsonData$main$temp_min - 273.15


#current weather in Bat yam
weatherTable = matrix(c(wind,cloudiness,humidity,temp,temp_min,temp_max),ncol=1,byrow=TRUE)
rownames(weatherTable)<-c("wind","cloudiness","humidity","temp","temp_min","temp_max")
colnames(weatherTable)<-c("value")
weatherTable <- as.table(weatherTable)
weatherTable

#Get forecast for 5 days 
jsonData = fromJSON("http://api.openweathermap.org/data/2.5/forecast?id=295548&APPID=0790aa655754a712e0fb78e2fa2277c7")

# Filled Density Plot
#convert temp degree from Kelvin to Celsius
d <- density(jsonData$list$main$temp-273.15)
plot(d, main="5 day forecast")
polygon(d, col="red", border="blue")


#Get the weather in several cities in Israel and display the temp
NetanyaJson = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=294071&APPID=0790aa655754a712e0fb78e2fa2277c7")
JerusalemJson = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=293198&APPID=0790aa655754a712e0fb78e2fa2277c7")
Qiryat_ShemonaJson = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=293825&APPID=0790aa655754a712e0fb78e2fa2277c7")
HaifaJson = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=294801&APPID=0790aa655754a712e0fb78e2fa2277c7")
TelavivJson = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=293397&APPID=0790aa655754a712e0fb78e2fa2277c7")
EilatJson = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=295277&APPID=0790aa655754a712e0fb78e2fa2277c7")
RishonJson = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=293703&APPID=0790aa655754a712e0fb78e2fa2277c7")
israelCurrentWeather = matrix(c(NetanyaJson$wind$speed,JerusalemJson$wind$speed,Qiryat_ShemonaJson$wind$speed,HaifaJson$wind$speed,TelavivJson$wind$speed,EilatJson$wind$speed,RishonJson$wind$speed,
                                NetanyaJson$clouds$all,JerusalemJson$clouds$all, Qiryat_ShemonaJson$clouds$all,HaifaJson$clouds$all,TelavivJson$clouds$all,EilatJson$clouds$all,RishonJson$clouds$all,
                                NetanyaJson$main$humidity,JerusalemJson$main$humidity,Qiryat_ShemonaJson$main$humidity,HaifaJson$main$humidity,TelavivJson$main$humidity,EilatJson$main$humidity,RishonJson$main$humidity,
                                NetanyaJson$main$temp,JerusalemJson$main$temp,Qiryat_ShemonaJson$main$temp,HaifaJson$main$temp,TelavivJson$main$temp, EilatJson$main$temp,RishonJson$main$temp,
                                NetanyaJson$main$temp_min,JerusalemJson$main$temp_min,Qiryat_ShemonaJson$main$temp_min,HaifaJson$main$temp_min,TelavivJson$main$temp_min,EilatJson$main$temp_min,RishonJson$main$temp_min,
                                NetanyaJson$main$temp_max,JerusalemJson$main$temp_max,Qiryat_ShemonaJson$main$temp_max,HaifaJson$main$temp_max,TelavivJson$main$temp_max,EilatJson$main$temp_max,RishonJson$main$temp_max),
                              ncol=6,nrow = 7 )
rownames(israelCurrentWeather) = c("Netanya","Jerusalem","Qiryat_Shemona", "Haifa","Telaviv","eilat" ,"Rishon")
colnames(israelCurrentWeather) = c("wind","cloudiness","humidity","temp","temp_min","temp_max")
israelCurrentWeather



#Get the dates
mydates <- as.Date(c(jsonData$list$dt_txt))
mydates

#Get current weather for cities
Netanya = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=294071&APPID=0790aa655754a712e0fb78e2fa2277c7")
Jerusalem = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=281184&APPID=0790aa655754a712e0fb78e2fa2277c7")
Ashdod = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=295629&APPID=0790aa655754a712e0fb78e2fa2277c7")
TelAviv = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=293397&APPID=0790aa655754a712e0fb78e2fa2277c7")
Eilat = fromJSON("http://api.openweathermap.org/data/2.5/weather?id=295277&APPID=0790aa655754a712e0fb78e2fa2277c7")

#Get the the city with the higher humidity
b = c(Netanya$main$humidity,Jerusalem$main$humidity,Ashdod$main$humidity,TelAviv$main$humidity,Eilat$main$humidity)
barplot(b,names.arg = c("Netanya","Jerusalem","Ashdod","TelAviv","Eilat") ,col = 'purple',main = "Humidity")

#Get the temp for cities
a = c(Netanya$main$temp-213.7,Jerusalem$main$temp-213.7,Ashdod$main$temp-213.7,TelAviv$main$temp-213.7,Eilat$main$temp-213.7)
barplot(a,names.arg = c("Netanya","Jerusalem","Ashdod","TelAviv","Eilat") ,col = 'yellow',main = "Hotest City")
