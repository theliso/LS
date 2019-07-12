
--Insert Projects
insert into project
    (projectName,description,creationDate)
values
    ('TwelveSports', 'Criacao de um sistema de Stream de desportos', '2017-03-01' );
insert into project
    (projectName,description,creationDate)
values
    ('LS', 'criação de um sistema de pedidos', '2017-01-01' );
insert into project
    (projectName,description,creationDate)
values
    ('AngryParrots', 'Criacao de um jogo digital', '2017-11-21');
insert into project
    (projectName,description,creationDate)
values
    ('SCP website', 'Criacao de um website para o SCP' , '2018-02-01');

--Insert Status
insert into status
    (description)
values
    ('open');
insert into status
    (description)
values
    ('close');

--Insert Issue
insert into issue
    (idProject,idStatus,issueName,created, updated)
values
    (1, 1, 'Logout Button', '2017-03-10', '2017-03-10');
insert into issue
    (idProject,idStatus,issueName,created, updated)
values
    (1, 2, 'Bug', '2017-03-11', '2017-03-11');
insert into issue
    (idProject,idStatus,issueName,created, updated)
values
    (2, 2, 'OMG', '2017-03-12', '2017-03-12');
insert into issue
    (idProject,idStatus,issueName,created, updated)
values
    (2, 2, 'Game not working', '2017-03-13', '2017-03-13');
insert into issue
    (idProject,idStatus,issueName,created, updated)
values
    (1, 2, 'Stream broken', '2017-03-14', '2017-03-14');
insert into issue
    (idProject,idStatus,issueName,created, updated)
values
    (1, 2, 'Black Screen', '2017-03-14', '2017-03-14');
insert into issue
    (idProject,idStatus,issueName,created, updated)
values
    (2, 1, 'Blue Screen', '2017-03-14', '2017-03-14');

-- Insert Comment
insert into comment
    (idIssue, idProject, commentDate, text)
values
    (2, 1, '2018-10-10', 'this bug is solved');
insert into comment
    (idIssue, idProject, commentDate, text)
values
    (2, 1, '2018-09-10', 'this bug is stupid, should be solved');

--Insert Label
insert into label
    (description)
values
    ('defect');
insert into label
    (description)
values
    ('new-functionality');
insert into label
    (description)
values
    ('exploration');

--insert Project_Label
insert into project_label
    (idProject, idLabel, color)
values
    (1, 1, 'red');


--insert Project_Label_Issue
insert into project_label_issue
    (idIIssue,idProjLab,idIProject, idPLLabel)
values
    (1, 1, 1, 1);