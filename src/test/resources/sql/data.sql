INSERT INTO task (id, title, description, due_date, completed)
VALUES (1, 'task_1', 'description_1', '2020-10-5', true),
       (2, 'task_2', 'description_2', '2010-8-3', false),
       (3, 'task_3', 'description_3', '2000-6-1', true);

SELECT setval('task_id_seq', (SELECT MAX(id) FROM task));