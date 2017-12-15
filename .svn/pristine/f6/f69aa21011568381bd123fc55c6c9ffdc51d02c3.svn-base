function newUser() {
	var user = {
		name : '',
		lastName : '',
		mail : '',
		active : 1,
		job : '',
		password : ''
	};
	return user;
};

function getFindState() {
	return [ {
		name : '',
		id : -1
	}, {
		name : 'Inactivo',
		id : 0
	}, {
		name : 'Activo',
		id : 1
	} ];
}

function getState() {
	return [ {
		name : 'Activo',
		id : 1
	}, {
		name : 'Inactivo',
		id : 0
	} ];
}

function stateValue($valueState) {
	var state = 0;
	if ($valueState == "Activo") {
		state = 1;
	}
	return state;
}