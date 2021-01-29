#Define funcoes para verificar a logica de execucao de uma aplicacao leitor/escritor

class ThreadSafe:
	def __init__(self):
		self.bucket = []
		for i in range(100000):
			self.bucket.append(0)

	def removeThreadSafe(self,i, result):
		'''Recebe o id do leitor. Verifica se a decisao de bloqueio esta correta.'''
		if(result==False and self.bucket[i] != 0):
			print("ERRO: remover bucket[" + str(i) + "] negado, mas existia value!")
		elif(result == True and self.bucket[i] == 0):
			print("ERRO: remover bucket[" + str(i) + "] permitido, mas não existia value!")
		self.bucket[i] = 0

	def insertThreadSafe(self,i,value, result):
		'''Recebe o id do leitor. Verifica se a decisao de bloqueio esta correta.'''
		if(result==False and self.bucket[i] == 0):
			print("ERRO: inserir bucket[" + str(i) + "] negado, mas estava limpo!")
		elif(result == True and self.bucket[i] != 0):
			print("ERRO: inserir bucket[" + str(i) + "] permitido, mas existia value!")
		self.bucket[i] = value
	