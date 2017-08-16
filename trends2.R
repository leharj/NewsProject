library(tm)
library(RWeka)
library(akmeans)
filename <- "news.txt"
con <- file(filename,"r")
line <- readLines(con)
len<-length(line)
data<-Corpus(VectorSource(line))
data<-tm_map(data,removePunctuation)
data<-tm_map(data,removeWords,stopwords("english"))
data<-tm_map(data,stripWhitespace)
dtm<-DocumentTermMatrix(data)
mat<-as.matrix(dtm)
results<-norm.sim.ksc(mat,length(line)/9,iter.max = 20)
results$cluster<-as.factor(results$cluster)
x<-summary(results$cluster)[summary(results$cluster)<15]
x<-x[x>=2]
i<-as.integer(names(x))
line1<-cbind(line,bucket=results$cluster)
x<-as.integer(line1[,2]) %in% i
line1<-line1[x,]
line1<-line1[order(as.integer(line1[,2])),]
y<-levels(as.factor(line1[,2]))
BigramTokenizer<-function(x) NGramTokenizer(x,Weka_control(min=2,max=2))
TrigramTokenizer<-function(x) NGramTokenizer(x,Weka_control(min=3,max=3))
trends<-vector(mode="character")
for(i in y){
  x<-line1[line1[,2]==i,1]
  corpus = VCorpus(VectorSource(x))
  corpus<-tm_map(corpus,removePunctuation)
  corpus1<-tm_map(corpus,removeWords,stopwords("english"))
  m<-as.matrix(DocumentTermMatrix(corpus1))
  a<-colnames(m)[sort.list(colSums(m),decreasing = TRUE)][1]
  scoreA<-sum(m[,a])*0.85
  m<-as.matrix(DocumentTermMatrix(corpus,control = list(tokenize=BigramTokenizer)))
  b<-colnames(m)[sort.list(colSums(m),decreasing = TRUE)][1]
  scoreB<-sum(m[,b])*1.5
  m<-as.matrix(DocumentTermMatrix(corpus,control = list(tokenize=TrigramTokenizer)))
  c<-colnames(m)[sort.list(colSums(m),decreasing = TRUE)][1]
  scoreC<-sum(m[,c])*2
  max=max(scoreA,scoreB,scoreC)
  if(scoreA==max)
    trends<-c(trends,a)
  if(scoreB==max)
    trends<-c(trends,b)
  if(scoreC==max)
    trends<-c(trends,c)
}
write(trends,"trends.txt",sep="\n")