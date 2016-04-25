library(ggplot2)
library(data.table)
require(stats)
library(RCurl)

#Get the data
url = "http://www.crowdflower.com/wp-content/uploads/2016/03/gender-classifier-DFE-791531.csv"
file = getURL(url)
data = read.csv(text = file)
data

#manipulate the data 
data <- data[data$X_unit_id < 815720437,] 
print(data$X_unit_id)

#Get female - male relation
length(data$gender=="male")
length(data$gender=="female")

#Display with barplot graph
a = c(length(data$gender=="male"),length(data$gender=="female"))
barplot(a,names.arg = c("male","female"),col = c('#A8C7F7',"red"), main="Amount of male and female use Twitter")

#Get all male twitter text
text_male = subset(data, data$gender=="male")
text_male = text_male$text
print(length(text_male))

#create list from text_male
text_male_list <- text_male[1:length(text_male)]

#create list of key and value 
#key = word , value = frequency
words = list()
for ( i in text_male_list) {
  # Merge the two lists.
  words <- c(words,unlist(strsplit(i," ")))
}
count = 0;
mylist = list()
for (word in words){
  for (xWord in words){
    if (word == xWord )
      count =  count + 1;
  }
  key <- word
  value <- count
  mylist[[ key ]] <- value
  count = 0;
}
#sort the data
DT1 = mylist
DT1 = data.table(x=c(names(mylist)),v=c(do.call(rbind, lapply(DT1, as.numeric))))
DT1 = DT1[order(-DT1$v),]
#create plot
print("male words")
#choose 6 words and display it with pie graph
#create plot
words_male = DT1[1:1200]
w1 = words_male[110]
w2 = words_male[164]
w3 = words_male[701]
w4 = words_male[272]
w5 = words_male[107]
w6 = words_male[1200]
w1
w2
w3
w4
w5
w6
#Simple Pie Chart
slices <- c(w1$v,w2$v,w3$v,w4$v,w5$v,w6$v)
lbls <- c(w1$x,w2$x,w3$x,w4$x,w5$x,w6$x)
pie(slices, labels = lbls, main="Pie Chart of Males frequency words")

#get all female twitter text
text_female = subset(data, data$gender=="female")
text_female = text_female$text
print(length(text_female))
v <- text_female[1:length(text_female)]
print(v)
#create list of key and value 
#key = word , value = frequency
x = list()
for ( i in v) {
  # Merge the two lists.
  x <- c(x,unlist(strsplit(i," ")))
}
count = 0;
mylist = list()
for (word in x){
  for (xWord in x){
    if (word == xWord)
      count =  count + 1;
  }
  key <- word
  value <- count
  mylist[[ key ]] <- value
  count = 0;
}
#sort the data
DT = mylist
DT = data.table(x=c(names(mylist)),v=c(do.call(rbind, lapply(DT, as.numeric))))
DT = DT[order(-DT$v),]
print("female words")
#create plot
words_female = DT[1:1200]
words_female
wf1 = words_female[152]
wf2 = words_female[153]
wf3 = words_female[216]
wf4 = words_female[227]
wf5 = words_female[909]
wf6 = words_female[67]
wf1$x
wf2$x
wf3$x
wf4$x
wf5$x
wf6$x

# Simple Pie Chart
slices <- c(wf1$v,wf2$v,wf3$v,w4$v,wf5$v,wf6$v)
lbls <- c(wf1$x,wf2$x,wf3$x,wf4$x,wf5$x,wf6$x)
pie(slices, labels = lbls, main="Pie Chart of females frequency words")
