USE tfg_gert_dam;

INSERT INTO Libro (id, isbn, portada, titulo, editorial_id, autor_id, tipolibro_id, fecha_salida, descripcion, precio, stock) VALUES
(1, '9788419645273', 'https://static.listadomanga.com/f74a768735e6250fb76a7a7afc2d7405.jpg', 'Chainsaw Man Vol. 1', 1, 3, 2, '2020-09-04', '', 9.00, 20),
(2, '9788419645274', 'https://static.listadomanga.com/e46b5ed8a607ef2f166876a6c8303940.jpg', 'Chainsaw Man Vol. 2', 1, 3, 2, '2020-11-13', '', 9.00, 20),
(3, '9788419645275', 'https://static.listadomanga.com/effdf7c3f579cb057602141112da379f.jpg', 'Chainsaw Man Vol. 3', 1, 3, 2, '2021-01-15', '', 9.00, 20),
(4, '9788419645276', 'https://static.listadomanga.com/a3f0463488556a66d0f89334d57ba62e.jpg', 'Chainsaw Man Vol. 4', 1, 3, 2, '2021-03-05', '', 9.00, 20),
(5, '9788499890951', 'https://imagessl1.casadellibro.com/a/l/s7/51/9788499890951.webp', 'Rebelión en la granja', 4, 5, 1, '2013-02-04', '', 11.00, 25),
(6, '9788499890944', 'https://imagessl4.casadellibro.com/a/l/s7/44/9788499890944.webp', '1984', 4, 5, 1, '2013-02-04', '', 11.00, 25),
(7, '9788491058038', 'https://imagessl8.casadellibro.com/a/l/s7/38/9788491058038.webp', 'La llamada de Cthulhu y otros cuentos', 5, 1, 1, '2026-03-12', '', 13.00, 25),
(8, '9788432237676', 'https://imagessl6.casadellibro.com/a/l/s7/76/9788432237676.webp', 'En las montañas de la locura', 6, 1, 1, '2021-02-03', '', 12.00, 25),
(9, '9781975392703', 'https://m.media-amazon.com/images/I/81ZzkjStT5L._UF350,350_QL50_.jpg', '86 - Eighty-Six Vol. 1', 3, 7, 4, '2019-03-26', '', 14.00, 25),
(10, '9781975303143', 'https://cdn.kobo.com/book-images/2ca3b404-76d9-490d-89f3-1b7e1570eede/1200/1200/False/86-eighty-six-vol-2-light-novel.jpg', '86 - Eighty-Six Vol. 2', 3, 7, 4, '2019-07-23', '', 14.00, 25),
(11, '9781975303112', 'https://cdn.kobo.com/book-images/a66cb117-da91-4527-bec0-e4c1f7227c1c/1200/1200/False/86-eighty-six-vol-3-light-novel.jpg', '86 - Eighty-Six Vol. 3', 3, 7, 4, '2019-11-19', '', 14.00, 25),
(12, '9781975303167', 'https://m.media-amazon.com/images/I/81W5cgvyKbL._UF1000,1000_QL80_.jpg', '86 - Eighty-Six Vol. 4', 3, 7, 4, '2020-03-31', '', 14.00, 25),
(13, '9791370131555', 'https://m.media-amazon.com/images/I/7117alIMmKL._SY342_.jpg', 'La cosa del pantano Vol. 1', 2, 8, 3, '2025-08-01', '', 50.00, 25),
(14, '9791370135201', 'https://www.panini.es/media/catalog/product/cache/758c09f6988c2b8c8b3c05da0c8e0025/s/v/svert008.jpg', 'La cosa del pantano Vol. 2', 2, 8, 3, '2026-04-16', '', 50.00, 25),
(15, '9791370132880', 'https://www.panini.es/media/catalog/product/cache/758c09f6988c2b8c8b3c05da0c8e0025/s/a/sardc004_es01.jpg', 'Archivos DC. Watchmen', 2, 8, 3, '2025-11-27', '', 50.00, 25),
(16, '9788411016346', 'https://www.panini.es/media/catalog/product/cache/758c09f6988c2b8c8b3c05da0c8e0025/s/o/sopro001_0.jpg', 'Providence Omnibus', 2, 8, 3, '2022-09-29', '', 60.00, 25),
(17, '9788419645277', 'https://static.listadomanga.com/1458016d5fdf745a2d79445abae7c7b8.jpg', 'Chainsaw Man Vol. 5', 1, 3, 2, '2021-05-14', '', 9.00, 20),
(18, '9788419645278', 'https://static.listadomanga.com/50ce897c80634565b101a08fde604795.jpg', 'Chainsaw Man Vol. 6', 1, 3, 2, '2021-07-09', '', 9.00, 20),
(19, '9788419645279', 'https://static.listadomanga.com/287b187337418acac9a94a84f5ff926b.jpg', 'Chainsaw Man Vol. 7', 1, 3, 2, '2021-08-06', '', 9.00, 20);

UPDATE Libro SET descripcion = 'Denji es un joven endeudado que trabaja como cazador de demonios junto a su perro demonio Pochita. Tras ser traicionado y asesinado, Pochita se fusiona con él, convirtiéndolo en Chainsaw Man, un híbrido humano-demonio que es reclutado por la División de Seguridad Pública para combatir amenazas demoníacas.' WHERE id = 1;
UPDATE Libro SET descripcion = 'Convertido en miembro oficial de la Seguridad Pública, Denji continúa cazando demonios bajo el mando de Makima. En este volumen se profundiza en su relación con Power y Aki mientras se enfrentan a nuevos y peligrosos enemigos, como el Demonio Murciélago y el Demonio Leech.' WHERE id = 2;
UPDATE Libro SET descripcion = 'La División Especial se enfrenta a un ataque coordinado por parte de asesinos aliados con el Demonio Pistola. Denji y sus compañeros deberán luchar por su supervivencia en una serie de enfrentamientos que cambian el rumbo de la historia.' WHERE id = 3;
UPDATE Libro SET descripcion = 'Tras los sucesos del asalto, Denji se enfrenta a nuevos enemigos y a revelaciones sobre el poder del Demonio Pistola. La violencia y las conspiraciones aumentan mientras Makima demuestra la magnitud de sus verdaderas capacidades.' WHERE id = 4;
UPDATE Libro SET descripcion = 'Fábula satírica en la que los animales de una granja se rebelan contra su dueño humano para crear una sociedad igualitaria. Con el tiempo, los cerdos que lideran la revolución reproducen los mismos vicios del poder que pretendían erradicar, en una alegoría del totalitarismo soviético.' WHERE id = 5;
UPDATE Libro SET descripcion = 'En una sociedad totalitaria gobernada por el Gran Hermano, Winston Smith lucha en silencio contra un régimen que controla la información, el lenguaje y hasta el pensamiento. La novela es una crítica profunda al autoritarismo y a la manipulación política.' WHERE id = 6;
UPDATE Libro SET descripcion = 'Recopilación de relatos de horror cósmico que incluye "La llamada de Cthulhu", donde se narra el descubrimiento de un culto dedicado a una entidad ancestral. Las historias exploran la insignificancia humana frente a fuerzas cósmicas incomprensibles.' WHERE id = 7;
UPDATE Libro SET descripcion = 'Un grupo de exploradores de la Universidad de Miskatonic viaja a la Antártida, donde descubre restos de una civilización alienígena ancestral. La expedición revela horrores indescriptibles y conocimientos que desafían la cordura humana.' WHERE id = 8;
UPDATE Libro SET descripcion = 'En la República de San Magnolia, la guerra parece librarse con drones autónomos, pero en realidad los combates los llevan a cabo jóvenes marginados conocidos como los Eighty-Six. La mayor Vladilena Milizé comienza a cuestionar el sistema al supervisar el Escuadrón Spearhead.' WHERE id = 9;
UPDATE Libro SET descripcion = 'Tras intensas batallas contra la Legión, el destino del Escuadrón Spearhead toma un giro inesperado. La historia profundiza en las consecuencias de la guerra y en la lucha por la dignidad de los Eighty-Six.' WHERE id = 10;
UPDATE Libro SET descripcion = 'Shin y sus compañeros continúan su lucha contra la Legión mientras se revelan más detalles sobre el origen de los drones enemigos. La novela explora el trauma de la guerra y los lazos forjados en el campo de batalla.' WHERE id = 11;
UPDATE Libro SET descripcion = 'La guerra contra la Legión se intensifica con nuevas amenazas tecnológicas y decisiones políticas que afectan el destino de los protagonistas. Vladilena y Shin deben afrontar duras realidades sobre el conflicto.' WHERE id = 12;
UPDATE Libro SET descripcion = 'El guionista Alan Moore reinventa al personaje de La Cosa del Pantano, explorando su verdadera naturaleza como entidad elemental ligada al Verde. La obra combina horror, ecología y metafísica en una etapa considerada fundamental del cómic moderno.' WHERE id = 13;
UPDATE Libro SET descripcion = 'Continúa la etapa de Alan Moore al frente de La Cosa del Pantano, profundizando en su dimensión cósmica y en sus enfrentamientos con fuerzas sobrenaturales, mientras se desarrollan temas filosóficos y románticos.' WHERE id = 14;
UPDATE Libro SET descripcion = 'Edición recopilatoria de Watchmen, la novela gráfica que narra una realidad alternativa donde los superhéroes existen y están desacreditados. La investigación del asesinato del Comediante destapa una conspiración que amenaza al mundo entero.' WHERE id = 15;
UPDATE Libro SET descripcion = 'Providence es una serie escrita por Alan Moore que reinterpreta el universo de H.P. Lovecraft a través de la historia de un periodista que investiga misteriosos sucesos en Nueva Inglaterra, descubriendo horrores que conectan con los Mitos de Cthulhu.' WHERE id = 16;
UPDATE Libro SET descripcion = 'Mientras la División Especial 4 se recupera de las bajas sufridas, Denji y sus compañeros se enfrentan a nuevos demonios cada vez más letales. El volumen profundiza en las motivaciones del Demonio Pistola y muestra el creciente alcance del poder y la influencia de Makima dentro de la organización.' WHERE id = 17;
UPDATE Libro SET descripcion = 'La investigación para dar con el Demonio Pistola conduce a la División a una operación internacional contra una poderosa organización criminal. Denji combate al Demonio de la Oscuridad en un enfrentamiento brutal que revela el abismo entre los cazadores y las entidades primigenias.' WHERE id = 18;
UPDATE Libro SET descripcion = 'Tras los traumáticos acontecimientos recientes, Denji queda emocionalmente devastado mientras Makima consolida su control. El volumen desvela la verdadera naturaleza de varios personajes clave y conduce a un clímax que redefine el destino de Chainsaw Man y su vínculo con el Demonio Pistola.' WHERE id = 19;

INSERT INTO Libro_Genero (libro_id, genero_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(7, 1),
(8, 1),
(13, 1),
(14, 1),
(16, 1),
(6, 4),
(7, 4),
(8, 4),
(9, 4),
(10, 4),
(11, 4),
(12, 4),
(13, 7),
(14, 7),
(1, 8),
(2, 8),
(3, 8),
(4, 8),
(9, 8),
(10, 8),
(11, 8),
(12, 8),
(7, 10),
(8, 10),
(13, 10),
(14, 10),
(9, 11),
(10, 11),
(11, 11),
(12, 11),
(15, 11),
(15, 12),
(16, 12),
(9, 13),
(10, 13),
(11, 13),
(12, 13),
(5, 14),
(6, 14),
(9, 14),
(10, 14),
(11, 14),
(12, 14),
(15, 14),
(9, 15),
(10, 15),
(11, 15),
(12, 15),
(5, 16),
(6, 16),
(15, 16),
(5, 17),
(13, 18),
(14, 18),
(15, 18),
(13, 19),
(14, 19),
(15, 19),
(17, 1),
(18, 1),
(19, 1),
(17, 8),
(18, 8),
(19, 8);

INSERT INTO Cliente VALUES
(1, "ADMIN", "Admin", "Admin", "admin", "admin@gmail.com", "$2a$10$i1iFhEGnu661zlgS1R9XsesWztCtJxAzwy6.SYGBWj.n.HdNaweA6"),
(2, "USER", "Gert", "Just", "gertswito", "gertjustgonzalezbreto@gmail.com", "$2a$10$KgUkyyXsAjJt3O9UC0dob.HAouNiC00CVovskxjX9/oaZIfRPjDYi");

INSERT INTO Direccion VALUES
(1, 2, "Calle Zamora", "12", "6D", "Fuenlabrada", "Madrid", "28941", "true"),
(2, 2, "Avenida de las Naciones", "27", "5A", "Fuenlabrada", "Madrid", "28943", "true");

INSERT INTO Venta (cliente_id, fecha, hora, precio_final, direccion_id) VALUES 
(2, '2026-02-26', '18:30:00', 18.00, 1),
(2, '2026-02-27', '19:30:00', 27.00, 2);

INSERT INTO LineaVenta (venta_id, libro_id, cantidad, precio_parcial, precio_total) VALUES 
(1, 1, 1, 9.00, 9.00),
(1, 2, 1, 9.00, 9.00),
(2, 3, 1, 9.00, 9.00),
(2, 4, 1, 9.00, 9.00),
(2, 17, 1, 9.00, 9.00);