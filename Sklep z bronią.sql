use [bd_java]
go

drop table if exists faktury, zamowienia, produkty, kategorie, pracownicy, klienci, firmy, adresy, kontakty

drop trigger if exists dodaj_zamowienie, dodaj_adres, dodaj_kontakt, aktualizuj_adres, aktualizuj_zamowienie, aktualizuj_kontakt

create table kontakty(
id_kontakt int primary key identity not null,
nr_tel_1 varchar(9) check(nr_tel_1 like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') not null,
nr_tel_2 varchar(9) check(nr_tel_2 like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
fax varchar(9) check(fax like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
email varchar(30) not null,
strona_www varchar(50)
)

create table adresy(
id_adres int primary key identity not null,
miejscowosc varchar(50) not null,
powiat varchar(50) not null,
wojewodztwo varchar(50) not null,
kod_pocztowy varchar(6) check(kod_pocztowy like '[0-9][0-9]-[0-9][0-9][0-9]') not null,
ulica varchar(50) not null,
nr_budynku int not null,
nr_lokalu int
)

create table firmy(
id_firma int primary key identity not null,
id_adres int foreign key references adresy(id_adres) not null,
nazwa_firma varchar(100) not null,
REGON varchar(9) check(REGON like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') not null,
NIP varchar(10) check(NIP like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') not null,
imie_wlasciciel varchar(40) not null,
nazwisko_wlasciciel varchar(50) not null
)

create table klienci(
id_klient int primary key identity not null,
id_adres int foreign key references adresy(id_adres) not null,
id_kontakt int foreign key references kontakty(id_kontakt) not null,
id_firma int foreign key references firmy(id_firma),
nazwisko varchar(50) not null,
imie varchar(40) not null,
PESEL varchar(11) check(PESEL like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') not null,
login_kl varchar(20) not null unique,
haslo_kl varchar(20) not null
)

create table pracownicy(
id_pracownik int primary key identity not null,
id_adres int foreign key references adresy(id_adres) not null,
id_kontakt int foreign key references kontakty(id_kontakt) not null,
nazwisko varchar(50) not null,
imie varchar(40) not null,
PESEL varchar(11) check(PESEL like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]') not null,
data_zatrudnienie date not null,
czy_aktywny varchar(3) not null,
login_pr varchar(20) not null unique,
haslo_pr varchar(20) not null,
status varchar(20) not null
)

create table kategorie(
id_kategoria int primary key identity not null,
nazwa_kategoria varchar(50) not null
)

create table produkty(
id_produkt int primary key identity not null,
id_kategoria int foreign key references kategorie(id_kategoria) not null,
nazwa_produkt varchar(50) not null,
producent varchar(20) not null,
model varchar(20) not null,
kaliber varchar(10) not null,
"dlugosc[mm]" int not null,
"masa[g]" int not null,
"dlugosc_lufy[mm]" int not null,
pojemnosc_magazynka int not null,
cena decimal(10,2) not null,
"stan[szt.]" int not null 
)

create table zamowienia(
id_zamowienie int primary key identity not null,
id_klient int foreign key references klienci(id_klient) not null,
id_pracownik int foreign key references pracownicy(id_pracownik),
id_produkt int foreign key references produkty(id_produkt) not null,
data_zlozenia_zamowienia datetime,
czy_przyjeto_zamowienie varchar(3),
czy_zaplacono varchar(3),
czy_zrealizowano_zamowienie varchar(3),
data_realizacji_zamowienia datetime
)

create table faktury(
id_faktura int primary key identity not null,
id_zamowienia int foreign key references zamowienia(id_zamowienie) not null,
data_wystawienia datetime not null,
data_sprzedazy datetime not null,
procent_VAT int not null,
wartosc_VAT decimal(10,2) not null,
cena_netto decimal(10,2) not null,
cena_brutto decimal(10,2) not null,
miejsce_wystawienia varchar(50) not null,
sposob_zaplaty varchar(30) not null
)

insert into kontakty values
('724610818',null,'224447777','kam.krol@op.pl','extremadamente.com'),
('565768421',null,null,'bober@gmail.com',null),
('791254032','646521365','228913214','elegancko@ug.edu.pl',null),
('598410167','598410156','597232200','poczta@rwscetus.pl','rwscetus.com'),
('564897987','789465413',null,'homo.sapiens@onet.pl',null),
('603222620','609107794',null,'biuro@grawib.eu','grawib.eu'),
('613847941',null,'591321461','sjw@xd.com',null),
('724651375','601359798',null,'lgbt.qwerty@xd.pl',null),
('845613447',null,null,'pizzapresto@pyszne.pl','prestopizza.pl'),
('554616547','798314566','226582343','koronawirus@global.com',null),
('541375645',null,null,'brakuje@mi.pomyslow',null),
('616353437','527984019',null,'konon@major.pl',null),
('616544775',null,null,'peeeewdiepie@pew.pew','jbzd.com')

insert into adresy values
('Kobylnica','S³upski','Pomorskie','76-251','Sosnowa','17',null),
('Kobylnica','S³upski','Pomorskie','76-251','Wodna','17',null),
('S³upsk','S³upski','Pomorskie','76-200','Jaracza','25',null),
('Warszawa','Warszawski','Mazowieckie','03-514','Piotra Skargi','52','107'),
('Gdañsk','Gdañski','Pomorskie','80-514','Dworska','58','24'),
('Mys³owice','Mys³owice','Œl¹skie','41-409','Juliusza Ligonia','83',null),
('Poznañ','Poznañski','Wielkopolskie','61-164','Orla','4','36'),
('Bia³ystok','Bia³ystok','Podlaskie','15-129','Ksiêcia Witolda','38','51'),
('Ruda Œl¹ska','Ruda Œl¹ska','Œl¹skie','41-706','Poli Gojawiczyñskiej','47','1'),
('Boronów','Lubliniecki','Œl¹skie','42-283','Powstañców','143',null),
('Brzeg','Brzeski','Opolskie','49-305','Fabryczna','87','17'),
('Warszawa','warszawski','Mazowieckie','01-376','DŸwigowa','43','13')

insert into firmy values
(2,'PPHU GRAWIB Wies³aw Grabuszyñski','123456789','8391125393','Wies³aw','Grabuszyñski'),
(3,'RWS CETUS Rafa³ Safader','467654134','8421003922','Rafa³','Safader'),
(6,'Presto PIZZA','465774531','9875643476','Powarzyñski','Filip'),
(10,'JBZD Admin RPJS','789314676','8983214464','Admin','Pies'),
(11,'LGBTQWERTXYZ+-*/=','932147767','3774576657','Nikt','Noone')

insert into klienci values
(4,1,4,'D¹browkski','Adrianna','96080485992','klient120','klient1'),
(7,7,5,'Wiciu','Dominik','66071237095','klient2','klient1'),
(9,5,2,'Soko³owski','Gabriel','99112201216','klient3','klient1'),
(5,4,3,'Wiœniewska','Bogumi³a','99112201216','klient4','klient1'),
(1,9,1,'Æwirko-Godycka','Beata','70080732165','klient5','klient1')

insert into pracownicy values
(5,10,'Adamska','Konstancja','95062633806','2020-04-30','tak','prac','prac','pracownik'),
(4,9,'Powaga','Filip','99122001345','2005-04-13','nie','pracownik1','pracownik2','pracownik'),
(9,7,'Maciejewski','Micha³','99122001345','2012-04-13','tak','pracownik','admin','pracownik'),
(12,8,'Koz³owska','Dorota','60102338713','2018-04-13','nie','admin1','adminka','admin'),
(8,6,'Skafander','Piotr','99122001345','2003-04-13','tak','admin','admin','admin')

insert into kategorie values
('pistolet maszynowy'),
('karabin snajperski'),
('karabinek'),
('pistolet'),
('strzelba')

insert into produkty values
(1,'GSG MP40 .22LR','GSG','MP40 .22LR','.22LR',825,3260,253,23,2090.00,15),
(3,'LUVO LA-15 LOW PROFILE','LUVO','LA-15','223 REM',1360,4370,425,30,6390.00,10),
(2,'CZ 750 S1M1 .308WIN','CZ','S1M1 .308 WIN','.308 WIN',1920,5800,660,10,9400.00,11),
(1,'Pioneer Arms Sporter S','Pioneer Arms','Sporter S','7.62x39',880,2870,415,30,2490.00,4),
(4,'75 SP-01 Shadow','CZ','75 SP-01 Shadow','9x19',207,1180,114,18,3060.00,25),
(4,'Fire Fly SD','GSG','Fire Fly SD','.22LR',183,720,99,10,1150.00,5),
(5,'Uzkon AS16','Uzkon','AS16','kal. 12',1060,3000,508,7,1270.00,6),
(5,'Uzkon AS42','Uzkon','AS42','kal. 12',850,3100,330,4,890.00,3),
(5,'Mossberg Maverick 88','Mossberg','Maverick 88','kal. 12',1041,3175,508,8,1290.00,12)

insert into zamowienia values
(5,1,6,'2019-03-11','tak','tak','tak','2019-03-22'),
(4,5,2,'2020-04-11','tak','tak','tak','2020-05-11'),
(1,3,5,'2020-05-16','tak','tak','nie',null),
(2,2,4,'2020-05-17','tak','tak','nie',null),
(3,4,7,'2020-05-18','tak','nie','nie',null)

insert into faktury values
(1,'2019-02-21','2020-05-12',23,215.04,934.96,1150.00,'Gdañsk','PayPal'),
(2,'2020-04-22','2020-05-12',23,1194.88,5195.12,6390.00,'Gdañsk','Blik'),
(3,'2020-05-17','2020-05-12',23,572.20,2487.80,3060.00,'Gdañsk','Gotówka'),
(4,'2020-05-17','2020-05-12',23,465.61,2024.39,2490.00,'Gdañsk','Natura')


select*from kontakty
select*from adresy
select*from firmy
select*from klienci
select*from pracownicy order by status
select*from kategorie
select*from produkty
select*from zamowienia
select*from faktury

go
create trigger dodaj_zamowienie on zamowienia instead of insert
as
declare @data datetime
declare @nulldata datetime
declare @idkl int
declare @idpr int
declare @idprod int
declare @data_zl datetime
declare @czy_p varchar(3)
declare @czy_zap varchar(3)
declare @czy_zr varchar(3)
select @idkl = id_klient from inserted
select @idpr = id_pracownik from inserted
select @idprod = id_produkt from inserted
select @data_zl = data_zlozenia_zamowienia from inserted
select @czy_p = czy_przyjeto_zamowienie from inserted
select @czy_zap = czy_zaplacono from inserted
select @czy_zr = czy_zrealizowano_zamowienie from inserted
select @data = data_realizacji_zamowienia from inserted
set @nulldata = null
if @data = '' or @data = null
begin
insert into zamowienia values(@idkl,@idpr,@idprod,@data_zl,@czy_p,@czy_zap,@czy_zr,@nulldata)
end
else 
begin
insert into zamowienia values(@idkl,@idpr,@idprod,@data_zl,@czy_p,@czy_zap,@czy_zr,@data)
end
go

go
create trigger dodaj_adres on adresy instead of insert
as
declare @msc varchar(50)
declare @pow varchar(50)
declare @woj varchar(50)
declare @kod varchar(6)
declare @ul varchar(50)
declare @nrb int
declare @nrl int
select @msc = miejscowosc from inserted
select @pow = powiat from inserted
select @woj = wojewodztwo from inserted
select @kod = kod_pocztowy from inserted
select @ul = ulica from inserted
select @nrb = nr_budynku from inserted
select @nrl = nr_lokalu from inserted
if @nrl = -1 or @nrl = null
begin
insert into adresy values
(@msc,@pow,@woj,@kod,@ul,@nrb,null)
end
else 
begin
insert into adresy values
(@msc,@pow,@woj,@kod,@ul,@nrb,@nrl)
end
go

go
create trigger dodaj_kontakt on kontakty instead of insert
as
declare @nr1 varchar(9)
declare @nr2 varchar(9)
declare @fax varchar(9)
declare @mail varchar(40)
declare @www varchar(50)
select @nr1 = nr_tel_1 from inserted
select @nr2 = nr_tel_2 from inserted
select @fax = fax from inserted
select @mail = email from inserted
select @www = strona_www from inserted
if @nr2 = ''
begin
	if @fax = ''
	begin
		if @www = ''
		begin
			insert into kontakty values
			(@nr1,null,null,@mail,null) --1
		end
		else
		begin
			insert into kontakty values
			(@nr1,null,null,@mail,@www) --2
		end
	end
	else if @fax != '' and @www = ''
	begin
		insert into kontakty values
		(@nr1,null,@fax,@mail,null) --3
	end
	else 
	begin
		insert into kontakty values
		(@nr1,null,@fax,@mail,@www) --4
	end
end
else if @nr2 != '' and @fax = ''
begin
	if @www = ''
	begin
	insert into kontakty values
	(@nr1,@nr2,null,@mail,null) --5
	end
	else
	begin
	insert into kontakty values
	(@nr1,@nr2,null,@mail,@www) --6
	end
end
else
begin
	if @www != ''
	begin
	insert into kontakty values
	(@nr1,@nr2,@fax,@mail,@www) --7
	end
	else
	begin
	insert into kontakty values
	(@nr1,@nr2,@fax,@mail,null) --8
	end
end
go

go
create trigger aktualizuj_kontakt on kontakty instead of update
as
declare @id int
declare @nr1 varchar(9)
declare @nr2 varchar(9)
declare @fax varchar(9)
declare @mail varchar(40)
declare @www varchar(50)
select @id = id_kontakt from inserted
select @nr1 = nr_tel_1 from inserted
select @nr2 = nr_tel_2 from inserted
select @fax = fax from inserted
select @mail = email from inserted
select @www = strona_www from inserted
if @nr2 = ''
begin
	if @fax = ''
	begin
		if @www = ''
		begin
			update kontakty set nr_tel_1 = @nr1, nr_tel_2=null, fax=null, email = @mail, strona_www=null where id_kontakt = @id
		end
		else
		begin
			update kontakty set nr_tel_1 = @nr1, nr_tel_2=null, fax=null, email = @mail, strona_www=@www where id_kontakt = @id
		end
	end
	else if @fax != '' and @www = ''
	begin
		update kontakty set nr_tel_1 = @nr1, nr_tel_2=null, fax=@fax, email = @mail, strona_www=null where id_kontakt = @id
	end
	else 
	begin
		update kontakty set nr_tel_1 = @nr1, nr_tel_2=null, fax=@fax, email = @mail, strona_www=@www where id_kontakt = @id
	end
end
else if @nr2 != '' and @fax = ''
begin
	if @www = ''
	begin
	update kontakty set nr_tel_1 = @nr1, nr_tel_2=@nr2, fax=null, email = @mail, strona_www=null where id_kontakt = @id
	end
	else
	begin
	update kontakty set nr_tel_1 = @nr1, nr_tel_2=@nr2, fax=null, email = @mail, strona_www=@www where id_kontakt = @id
	end
end
else
begin
	if @www != ''
	begin
	update kontakty set nr_tel_1 = @nr1, nr_tel_2=@nr2, fax=@fax, email = @mail, strona_www=@www where id_kontakt = @id
	end
	else
	begin
	update kontakty set nr_tel_1 = @nr1, nr_tel_2=@nr2, fax=@fax, email = @mail, strona_www=null where id_kontakt = @id
	end
end
go

go
create trigger aktualizuj_zamowienie on zamowienia instead of update
as
declare @id int
declare @data datetime
declare @idkl int
declare @idpr int
declare @idprod int
declare @data_zl datetime
declare @czy_p varchar(3)
declare @czy_zap varchar(3)
declare @czy_zr varchar(3)
select @idprod = id_produkt from inserted
select @id = id_zamowienie from inserted
select @idkl = id_klient from inserted
select @idpr = id_pracownik from inserted
select @data_zl = data_zlozenia_zamowienia from inserted
select @czy_p = czy_przyjeto_zamowienie from inserted
select @czy_zap = czy_zaplacono from inserted
select @czy_zr = czy_zrealizowano_zamowienie from inserted
select @data = data_realizacji_zamowienia from inserted
if @data = '' or @data = null
begin
update zamowienia set id_klient=@idkl, id_pracownik=@idpr, id_produkt = @idprod,data_zlozenia_zamowienia = @data_zl,
czy_przyjeto_zamowienie = @czy_p, czy_zaplacono= @czy_zap, czy_zrealizowano_zamowienie = @czy_zr,
data_realizacji_zamowienia = null where id_zamowienie = @id
end
else 
begin
update zamowienia set id_klient=@idkl, id_pracownik=@idpr, id_produkt = @idprod, data_zlozenia_zamowienia = @data_zl,
czy_przyjeto_zamowienie = @czy_p, czy_zaplacono= @czy_zap, czy_zrealizowano_zamowienie = @czy_zr,
data_realizacji_zamowienia = @data where id_zamowienie = @id
end
go

go
create trigger aktualizuj_adres on adresy instead of update
as
declare @id int
declare @msc varchar(50)
declare @pow varchar(50)
declare @woj varchar(50)
declare @kod varchar(6)
declare @ul varchar(50)
declare @nrb int
declare @nrl int
select @id = id_adres from inserted
select @msc = miejscowosc from inserted
select @pow = powiat from inserted
select @woj = wojewodztwo from inserted
select @kod = kod_pocztowy from inserted
select @ul = ulica from inserted
select @nrb = nr_budynku from inserted
select @nrl = nr_lokalu from inserted
if @nrl = -1 or @nrl = null
begin
update adresy set miejscowosc = @msc, powiat = @pow, wojewodztwo = @woj, kod_pocztowy = @kod,
								ulica = @ul, nr_budynku = @nrb, nr_lokalu = null  where id_adres = @id
end
else 
begin
update adresy set miejscowosc = @msc, powiat = @pow, wojewodztwo = @woj, kod_pocztowy = @kod,
								ulica = @ul, nr_budynku = @nrb, nr_lokalu = @nrl  where id_adres = @id
end
go

update adresy set powiat = 'slupski' , nr_lokalu = -1 where id_adres = 1


