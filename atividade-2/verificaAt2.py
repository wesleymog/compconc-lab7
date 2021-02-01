#Define funcoes para verificar a logica de execucao de uma aplicacao consumidor/produtor

class CP:
	def __init__(self):
		self.produtores = 0
		self.consumidores = 0
		self.full = 0
		#0 - não iniciado
		#1 - vazio
		#2 - cheio

	def consumidorBloqueado(self,id):
		'''Recebe o id do consumidor. Verifica se a decisao de bloqueio esta correta.'''
		if(self.produtores == 0 and self.consumidores == 0 and self.full != 1):
			print("ERRO: consumidor " + str(id) + " bloqueado quando o buffer estava cheio!")
		
	def produtorBloqueado(self,id):
		'''Recebe o id do produtor. Verifica se a decisao de bloqueio esta correta.'''
		if(self.produtores == 0 and self.consumidores == 0 and self.full != 2):
			print("ERRO: produtor " + str(id) + " bloqueado quando o buffer estava vazio!")

	def consumidorLendo(self,id):
		'''Recebe o id do consumidor, verifica se pode ler e registra que esta lendo.'''
		if(self.produtores>0 and self.consumidores >0):
			print("ERRO: consumidor " + str(id) + " esta lendo quando ha produtor escrevendo!")
		self.consumidores+=1
		self.full = 1
		

	def produtorEscrevendo(self,id):
		'''Recebe o id do produtor, verifica se pode escrever e registra que esta escrevendo'''
		if(self.produtores>0 or self.consumidores>0):
			print("ERRO: produtor " + str(id) + " esta escrevendo quando ha outro produtor ou consumidores!")
		self.produtores+=1
		self.full = 2

	def consumidorSaindo(self,id):
		'''Recebe o id do consumidor e registra que terminou a leitura.'''
		self.consumidores-=1

	def produtorSaindo(self,id):
		'''Recebe o id do produtor e registra que terminou a leitura.'''
		self.produtores-=1


