library(tm)
library(RWeka)
library(akmeans)
filename <- "news1.txt"
con <- file(filename,"r")
line <- readLines(con)
line<-unique(line)
line <- gsub("'s","",line)
len<-length(line)
fact = 8
if(len>700)
  fact = 10
data<-Corpus(VectorSource(line))
data<-tm_map(data,removePunctuation)
data<-tm_map(data,removeWords,stopwords("english"))
data<-tm_map(data,stripWhitespace)
dtm<-DocumentTermMatrix(data)
mat<-as.matrix(dtm)
a<-colSums(mat);
a<-sort(a,decreasing = TRUE)
df<-data.frame(value = 1/a);
write.table(df,file="weights.txt",sep=" ",row.names = TRUE,col.names = FALSE,quote = FALSE)
dtm<-weightTfIdf(dtm)
mat<-as.matrix(dtm)
results<-norm.sim.ksc(mat,length(line)/10,iter.max = 10)
results$cluster<-as.factor(results$cluster)
x<-summary(results$cluster)
x<-x[x>3]
x<-x[x<30]
i<-as.integer(names(x))
i<-na.omit(i)
line1<-cbind(line,bucket=results$cluster)
x<-as.integer(line1[,2]) %in% i
line1<-line1[x,]
line1<-line1[order(as.integer(line1[,2])),]
y<-levels(as.factor(line1[,2]))
BigramTokenizer<-function(x) NGramTokenizer(x,Weka_control(min=2,max=2))
TrigramTokenizer<-function(x) NGramTokenizer(x,Weka_control(min=3,max=3))
QuadgramTokenizer<-function(x) NGramTokenizer(x,Weka_control(min=4,max=4))
trends<-vector(mode="character")
for(i in y){
  x<-line1[line1[,2]==i,1]
  if(length(x)>2){
    len<-length(x)
    corpus = VCorpus(VectorSource(x))
    corpus<-tm_map(corpus,removePunctuation)
    corpus1<-tm_map(corpus,removeWords,stopwords("english"))
    m<-as.matrix(DocumentTermMatrix(corpus1))
    drop <- colnames(m)[nchar(colnames(m))<7]
    m<-m[,!(colnames(m) %in% drop)]
    a<-colnames(m)[colSums(m)>max(4,len/2)]
    trends<-c(trends,a)
    m<-as.matrix(DocumentTermMatrix(corpus,control = list(tokenize=BigramTokenizer)))
    a<-colnames(m)[colSums(m)>max(len/4,2)]
    trends<-c(trends,a)
    m<-as.matrix(DocumentTermMatrix(corpus,control = list(tokenize=TrigramTokenizer)))
    a<-colnames(m)[colSums(m)>2]
    trends<-c(trends,a)
    m<-as.matrix(DocumentTermMatrix(corpus,control = list(tokenize = QuadgramTokenizer)))
    a<-colnames(m)[colSums(m)>2]
    trends<-c(trends,a)
  }
}
write(trends,"trendsNational.txt",append = TRUE,sep="\n")