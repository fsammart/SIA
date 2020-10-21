europe <- read.csv("~/ITBA/SIA/PCA/europe.csv", row.names=1)
boxplot(europe)
europe_st <- scale(europe)
boxplot(europe_st)
res <- princomp(europe_st, cor=FALSE, score=TRUE)
install.packages("devtools")
install_github("vqv/ggbiplot")
install.packages("ggbiplot")
library(ggbiplot)
ggbiplot(res)
ggbiplot(res, labels=rownames(europe_st))




