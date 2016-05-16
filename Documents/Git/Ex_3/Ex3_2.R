folder = '/Users/Hila/Documents/ML/Targil3'
setwd(folder)
require(igraph)
ga.data = read.csv('NationalNames1.csv',header = T)
ga_names =  graph.data.frame(ga.data,directed = F)
summary(ga_names)
V(ga_names)$name

#Remove self-Loops is exist 
ga_names = simplify(ga_names)

#Calculate betweenness 
ga_bet = betweenness(ga_names)
ga_bet = sort(ga_bet,decreasing = T)
names(ga_bet[1])

#Calculate closeness 
ga_close = closeness(ga_names)
ga_close = sort(ga_close, decreasing = T)
names(ga_close[1])

#Calculate eigenvector
ga_eigen = evcent(ga_names)
ga_eigen = sort(ga_eigen$vector, decreasing = T)
names(ga_eigen[1])

#Find commuinty with Girvan-Newman community detection
fc = edge.betweenness.community(ga_names)

#Cheack what is the modularity
fc$modularity

#What partition is the best?
max(fc$modularity)
which.max(fc$modularity)

#Color nodes by partitions 
memb = membership(fc)
plot(ga_names, vertex.size=7, vertex.label=NA,
     vertex.color=memb, asp=FALSE)

#How many communities received
max(levels(as.factor(memb)))

#What size of each commuinty
summary(as.factor(memb))


#Find commuinty with Multi-Level algorithm
#This function implements the multi-level modularity optimization algorithm for finding community structure.
ml = multilevel.community(ga_names)

#Cheack what is the modularity
ml$modularity

#What partition is the best?
max(ml$modularity)
which.max(ml$modularity)

#Color nodes by partitions 
memb = membership(ml)
plot(ga_names, vertex.size=5, vertex.label=NA,
     vertex.color=memb, asp=FALSE)

#How many communities received
max(levels(as.factor(memb)))

#What size of each commuinty
summary(as.factor(memb))


#Find commuinty with propagating labels algorithm
#This is a fast, nearly linear time algorithm for detecting community structure in networks.
#In works by labeling the vertices with unique labels and then updating the labels by majority voting in the neighborhood of the vertex.
pl = label.propagation.community(ga_names)

#Cheack what is the modularity
pl$modularity

#What partition is the best?
max(pl$modularity)
which.max(pl$modularity)

#Color nodes by partitions 
memb = membership(pl)

plot(ga_names, vertex.size=8, vertex.label=NA,
     vertex.color=memb, asp=FALSE)

#How many communities received
max(levels(as.factor(memb)))

#What size of each commuinty
summary(as.factor(memb))