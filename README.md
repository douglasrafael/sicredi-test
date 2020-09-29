# Sicredi Test - My Event App
[![License][license-image]][license-url] [![Commit][last-commit-image]][last-commit-url] [![Releases][releases-image]][releases-url] [![Contributors][contributors-image]][contributors-url]

[![Download][apk-download]][apk-download-url]

-----

### Este projeto foi realizado para o teste de desenvolvedor Android da [Sicredi](https://github.com/WoopSicredi/jobs/issues/1). 

Foi desenvolvido em Kotlin utilizando os mais avançados recursos que a linguagem oferece como Extensions e Coroutines, como também, os mais recentes recursos das bibliotecas Jetpack, como Navigation e Databinding.

Com base nos requerimentos e instruções fornecidas para desenvolvimento foi implementado as seguintes funcionalidades:
- Listagem de eventos;
- Busca por eventos;
- Exibição de detalhes do evento, como descrição detalhada e exibição de localização por meio de mapa.
- Favoritar um evento;
- Realizar check-in em eventos;
- Listagem de eventos que realizou check-in.
- Compartilhamento de evento.


## Começando
A maneira mais fácil de visualizar o aplicativo em ação é baixando e instalando o [apk >>][apk-download-url]

Se deseja visualizar o código e os detalhes de implementação você pode utilizar o Android Studio:
#### Pré-requisitos
 - Android SDK v30
 - Android Studio 3.5+

Para baixar o projeto:
1. **Clone o repositório**
   ```console
   git clone https://github.com/douglasrafael/sicredi-test
   ```
2. **Abra o projeto no Android Studio:**
   - No menu do Android Studio, clique em `File > Open`.
   - Como alternativa, na tela "Bem-vindo", clique em > `Open an existing Android Studio project`.
   - Selecione a pasta do projeto e clique em OK.


## Modelo de Dados
A aplicação é composto pelas entidades Event, People e CheckIn.

<img src="https://i.imgur.com/Fa4Hq9x.png" />


## Arquitetura
Foi utilizado **MVVM** _(Model-View-ViewModel)_. Este Pattern suporta ligação bidirecional entre View e ViewModel, com isto é possível termos propagação automática de mudanças e LiveData (Objeto Observável) é utilizado para essa finalidade. 


<img src="https://i.imgur.com/mGNkir2.png" />


Como pode ser visto na imagem anterior, a arquitetura divide-se em três camadas:

1. **View** - São Activity e Fragment, ou seja, é onde fica os componentes de interface que o usuário interage. Essa camada se comunica exclusivamente com a ViewModel.

2. **ViewModel** - É a camada responsável por expor métodos, comandos e propriedades que mantém o estado da View, ela se comunica com a camada de dados e retorna resultados de uma ação por meio de objetos observáveis.

3. **Model** - É a camada responsável pela regra de negócio da aplicação, persistência de dados e comunicação com serviços externos. Ela exclusivamente responde comandos solicitados pela ViewModel.

## Testes
Nesse primeiro momento, foi realizado apenas teste de interface. O ideal é que sejam realizados testes de ViewModel também.


## Principais Bibliotecas Utilizadas

- [Koin](https://insert-koin.io/) Utilizada para Injeção de Depência (DI), ela cria as instâncias em tempo de execução.
- [Android Material Compontes](https://material.io/components) Possibilita a utilização dos mais novos componentes do Material Design.
- [Retrofit](https://square.github.io/retrofit/) Client HTTP. Utilizada para fazer comunicação com API Rest.
- [Coil](https://github.com/coil-kt/coil/) Utilizada para carregamento de imagens. Ela é escrita 100% em Kotlin e concorre diretamente com Glide e Picasso.
- [Koleton](https://github.com/ericktijerou/koleton) Fornece uma maneira fácil de mostrar o esqueleto de qualquer visualização, útil para estados de loading.
- [Alerter](https://github.com/Tapadoo/Alerter/branches) Utilizada para exibir alertas em estilo de notificações dentro do aplicativo.
- [Timber](https://github.com/JakeWharton/timber) Centralizador de logger.
- [Play Sevices Maps](https://developers.google.com/maps/documentation/android-sdk/start) Usada para funcionalidades de mapa.
- [Valifi](https://github.com/mlykotom/valifi) Usada para validação de formulário. Possui suporte a databinding.
- [mockito](https://github.com/mlykotom/valifi) Usada para realização dos testes de unidade.


### Screenshots

<img align="left" src="https://i.imgur.com/su7fhew.png" width="280" />
<img align="left" src="https://i.imgur.com/K6nSPAF.png" width="280" />
<img align="left" src="https://i.imgur.com/8AlgoNE.png" width="280" />
<img align="left" src="https://i.imgur.com/arLjq8j.png" width="280" />
<img align="left" src="https://i.imgur.com/XFh2thp.png" width="280" />
<img align="left" src="https://i.imgur.com/dlKQnPi.png" width="280" />
<img align="left" src="https://i.imgur.com/84OJfCh.png" width="280" />

----

[//]: # (These are reference links used in the body of this note.)
[license-image]: https://img.shields.io/badge/license-Apache%202-blue.svg
[license-url]: https://github.com/douglasrafael/sicredi-test/blob/master/LICENSE
[last-commit-image]: https://img.shields.io/github/last-commit/douglasrafael/sicredi-test.svg
[last-commit-url]: https://github.com/douglasrafael/sicredi-test/commits
[releases-image]: https://img.shields.io/github/release-date/douglasrafael/sicredi-test.svg
[releases-url]: https://github.com/douglasrafael/sicredi-test/releases
[contributors-image]: https://img.shields.io/github/contributors/douglasrafael/sicredi-test.svg
[contributors-url]: https://github.com/douglasrafael/sicredi-test/graphs/contributors
[apk-download]: https://img.shields.io/badge/download%20apk-DEBUG-blue.svg?style=for-the-badge&logo=android
[apk-download-url]: https://github.com/douglasrafael/sicredi-test/releases/download/1.0.0/sicredi-test_v1.0.0-debug.apk