-- Posts data base

create table Project(
    id serial primary key,
    projectName varchar(20) not null,
    description varchar(50),
	creationDate timestamp not null
);

create table Status(
    id serial primary key, 
    description varchar(10) not null check (description in ('open', 'close')) 
);

create table Issue(
    idIssue serial,
    idProject integer,
    idStatus integer,
    issueName varchar(20) not null,
    description varchar(255),
    created timestamp null,
    updated timestamp not null,
    primary key(idIssue, idProject),
    foreign key (idProject) references Project(id),
    foreign key (idStatus) references Status(id)
);

create table Comment(
    idComment serial,
    idIssue integer,
	idProject integer, 
    commentDate timestamp not null,
    text varchar(255) not null,
    primary key(idComment, idIssue, idProject),
    foreign key (idIssue, idProject) references Issue(idIssue, idProject)
);

create table Label(
    id serial primary key,
    description varchar(30)
);

create table Project_Label(
	idProject integer not null,
	idLabel integer not null,
	color varchar(10) not null,
	primary key (idProject,idLabel),
	foreign key (idProject) references Project(id),
	foreign key (idLabel) references Label(id)
);

create table Project_Label_Issue(
	idIIssue integer not null,
	idProjLab integer not null,
	idIProject integer not null,
	idPLLabel integer not null,
	primary key (idIIssue,idProjLab, idPLLabel,idIProject),
	foreign key (idIIssue,idIProject) references Issue(idIssue,idProject),
	foreign key (idProjLab,idPLLabel) references Project_Label(idProject, idLabel)
);
