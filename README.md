<h1>Threaded Canteen Simulation</h1>
Projekt realizuje symulacje pracy stołówki uczelnianej, gdzie agentami opartymi o wątki są jej klienci, kucharze wydający dania oraz kasjerzy.
<br> <br>
Projekt zrealizowany w ramach zajęć laboratoryjnych „Języki Programowania”, Politechnika Wrocławska, semestr zimowy 2024/2025. 
<h3>Użyte narzędzia:</h3>
<ul>
  <li>Java, ver. 17</li>
  <li>JavaFX, ver. 21.0.6</li>
</ul>
<h2>Pierwsze kroki</h2>
<h3>Wymagania:</h3>
<ul>
  <li>Java, ver. 17</li>
</ul>	
<h3>Instalacja i uruchomienie aplikacji</h3>
<ul>
  <li>Pobierz cały projekt, a następnie uruchom klasę "GUI.java" w pakiecie <i>pl.edu.pwr.lczerwinski.threaded_canteen.gui</i></li>
</ul>

<h2>Założenia projektowe</h2>
Na stołówce zlokalizowano dwa punkty wydawania dań. Do tych punktów ustawiają się kolejki przychodzących klientów. Po otrzymaniu dania Klient przechodzi do jednej z czterech kas, by dokonać opłaty za danie. Po zapłacie Klient kieruje się do stołu, by zająć miejsce. W stołówce istnieją dwa długie stoły, przy których po każdej z dwóch dłuższych stron istnieje n miejsc (czyli przy jednym stole może zasiąść 2*n klientów). Klient próbuje zająć wolne miejsce sekwencyjnie, startując od jakiegoś wybranego punktu, a po zajęciu miejsca konsumuje danie (tj. przez chwilę przy tym stoliku siedzi). Następnie klient opuszcza stołówkę.
<ol type=1>
  <li>Każdy klient jest wątkiem, który cyklicznie udaje się do stołówki, by zamówić i skonsumować danie.</li>
  <li>Kolejki przy punktach wydawania i kasach są współdzielonym zasobem. Klient wybiera zawsze najkrótszą dostępną kolejkę.</li>
  <li>Każda kolejka wydawania jest obsługiwana przez jednego Kucharza. Kucharz jest wątkiem, który pobiera Klienta z kolejki i przekazuje mu jedno z dań dnia. </li>
  <li>Każda kasa jest obsługiwana przez Kasjera. Kasjer jest wątkiem, który działa podobnie do Kucharza - obsługuje kolejkę Klientów chcących zapłacić za danie. Kasjer sprawdza jedynie, jakie danie wziął Klient, dodając je do spisu wydanych dań. </li>
  <li>Pracownicy mogą podjąć decyzje o udaniu się na przerwę. Zanim jednak wszyscy klienci obecni już w kolejce zostaną obsłużeni, jednak zablokowana zostanie możliwość ustawiania się do niej.</li>
  <li>Wszystkie czasy operacji są losowe, jednak zawierające się w określonych przedziałach czasowych.</li>
</nl>
